/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.atomicfu.compiler.backend

import org.jetbrains.kotlin.backend.common.extensions.*
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.ir.*
import org.jetbrains.kotlin.ir.backend.js.ir.JsIrBuilder.buildValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.*
import org.jetbrains.kotlin.ir.expressions.impl.*
import org.jetbrains.kotlin.ir.symbols.*
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.types.impl.*
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.*
import org.jetbrains.kotlin.types.*
import org.jetbrains.kotlin.util.capitalizeDecapitalize.*

// TODO: get rid of TransformerUtils at all

private const val KOTLIN = "kotlin"
private const val GET = "get"
private const val SET = "set"

private val AFU_ARRAY_CLASSES: Map<String, String> = mapOf(
    "AtomicIntArray" to "IntArray",
    "AtomicLongArray" to "LongArray",
    "AtomicBooleanArray" to "BooleanArray",
    "AtomicArray" to "Array"
)

internal fun buildCall(
    startOffset: Int,
    endOffset: Int,
    target: IrSimpleFunctionSymbol,
    type: IrType? = null,
    origin: IrStatementOrigin? = null,
    typeArguments: List<IrType> = emptyList(),
    valueArguments: List<IrExpression?> = emptyList()
): IrCall =
    IrCallImpl(
        startOffset,
        endOffset,
        type ?: target.owner.returnType,
        target,
        typeArguments.size,
        valueArguments.size,
        origin
    ).apply {
        typeArguments.let {
            it.withIndex().forEach { (i, t) -> putTypeArgument(i, t) }
        }
        valueArguments.let {
            it.withIndex().forEach { (i, arg) -> putValueArgument(i, arg) }
        }
    }

internal fun IrFactory.buildBlockBody(statements: List<IrStatement>) =
    createBlockBody(UNDEFINED_OFFSET, UNDEFINED_OFFSET, statements)

internal fun buildSetField(
    symbol: IrFieldSymbol,
    receiver: IrExpression?,
    value: IrExpression,
    superQualifierSymbol: IrClassSymbol? = null
): IrSetField =
    IrSetFieldImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        symbol,
        receiver,
        value,
        value.type,
        IrStatementOrigin.GET_PROPERTY,
        superQualifierSymbol
    )

internal fun buildGetField(
    symbol: IrFieldSymbol,
    receiver: IrExpression?,
    superQualifierSymbol: IrClassSymbol? = null,
    type: IrType? = null
): IrGetField =
    IrGetFieldImpl(
        UNDEFINED_OFFSET,
        UNDEFINED_OFFSET,
        symbol,
        type ?: symbol.owner.type,
        receiver,
        IrStatementOrigin.GET_PROPERTY,
        superQualifierSymbol
    )

internal fun buildSimpleType(
    symbol: IrClassifierSymbol,
    typeParameters: List<IrType>
): IrSimpleType =
    IrSimpleTypeImpl(
        classifier = symbol,
        hasQuestionMark = false,
        arguments = typeParameters.map { makeTypeProjection(it, Variance.INVARIANT) },
        annotations = emptyList()
    )

internal fun buildGetValue(
    startOffset: Int,
    endOffset: Int,
    symbol: IrValueSymbol
): IrGetValue =
    IrGetValueImpl(
        startOffset,
        endOffset,
        symbol.owner.type,
        symbol
    )

internal fun IrPluginContext.buildConstNull() = IrConstImpl.constNull(UNDEFINED_OFFSET, UNDEFINED_OFFSET, irBuiltIns.anyNType)

internal fun IrExpression.isConstNull() = this is IrConst<*> && this.kind.asString == "Null"

internal fun IrField.getterName() = "<get-${name.asString()}>"
internal fun IrField.setterName() = "<set-${name.asString()}>"

internal fun IrFunctionAccessExpression.getValueArguments() =
    (0 until valueArgumentsCount).map { i ->
        getValueArgument(i)
    }

internal fun IrValueParameter.capture() = buildGetValue(UNDEFINED_OFFSET, UNDEFINED_OFFSET, symbol)

internal fun IrPluginContext.buildGetterType(valueType: IrType): IrSimpleType =
    buildSimpleType(
        irBuiltIns.functionN(0).symbol,
        listOf(valueType)
    )

internal fun IrPluginContext.buildSetterType(valueType: IrType): IrSimpleType =
    buildSimpleType(
        irBuiltIns.functionN(1).symbol,
        listOf(valueType, irBuiltIns.unitType)
    )

private fun buildSetField(backingField: IrField, ownerClass: IrExpression?, value: IrGetValue): IrSetField {
    val receiver = if (ownerClass is IrTypeOperatorCall) ownerClass.argument as IrGetValue else ownerClass
    return buildSetField(
        symbol = backingField.symbol,
        receiver = receiver,
        value = value
    )
}

private fun buildGetField(backingField: IrField, ownerClass: IrExpression?): IrGetField {
    val receiver = if (ownerClass is IrTypeOperatorCall) ownerClass.argument as IrGetValue else ownerClass
    return buildGetField(
        symbol = backingField.symbol,
        receiver = receiver
    )
}

private fun IrPluginContext.buildDefaultPropertyAccessor(name: String): IrSimpleFunction =
    irFactory.buildFun {
        startOffset = UNDEFINED_OFFSET
        endOffset = UNDEFINED_OFFSET
        this.origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
        this.visibility = DescriptorVisibilities.LOCAL
        this.isInline = true
        this.name = Name.identifier(name)
    }

internal fun IrPluginContext.buildArrayElementAccessor(
    arrayField: IrField,
    arrayGetter: IrCall,
    index: IrExpression,
    isSetter: Boolean
): IrExpression {
    val valueType = arrayField.type
    val functionType = if (isSetter) buildSetterType(valueType) else buildGetterType(valueType)
    val returnType = if (isSetter) irBuiltIns.unitType else valueType
    val name = if (isSetter) arrayField.setterName() else arrayField.getterName()
    val accessorFunction = buildDefaultPropertyAccessor(name).apply {
        val valueParameter = buildValueParameter(this, name, 0, valueType)
        this.valueParameters = if (isSetter) listOf(valueParameter) else emptyList()
        body = irFactory.buildBlockBody(
            listOf(
                if (isSetter) {
                    val setSymbol = referenceFunction(referenceArrayClass(arrayField.type as IrSimpleType), SET)
                    buildCall(
                        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                        target = setSymbol,
                        type = irBuiltIns.unitType,
                        origin = IrStatementOrigin.LAMBDA,
                        valueArguments = listOf(index, valueParameter.capture())
                    ).apply {
                        this.dispatchReceiver = arrayGetter
                    }
                } else {
                    val getField = buildGetField(arrayField, arrayGetter.dispatchReceiver)
                    val getSymbol = referenceFunction(referenceArrayClass(arrayField.type as IrSimpleType), GET)
                    buildCall(
                        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                        target = getSymbol,
                        type = valueType,
                        origin = IrStatementOrigin.LAMBDA,
                        valueArguments = listOf(index)
                    ).apply {
                        dispatchReceiver = getField
                    }
                }
            )
        )
        this.returnType = returnType
    }
    return IrFunctionExpressionImpl(
        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
        functionType,
        accessorFunction,
        IrStatementOrigin.LAMBDA
    )
}

internal fun IrPluginContext.buildFieldAccessor(
    field: IrField,
    dispatchReceiver: IrExpression?,
    isSetter: Boolean
): IrExpression {
    val valueType = field.type
    require(valueType.isPrimitiveType())
    val functionType = if (isSetter) buildSetterType(valueType) else buildGetterType(valueType)
    val returnType = if (isSetter) irBuiltIns.unitType else valueType
    val name = if (isSetter) field.setterName() else field.getterName()
    val accessorFunction = buildDefaultPropertyAccessor(name).apply {
        val valueParameter = buildValueParameter(this, name, 0, valueType)
        valueParameters = if (isSetter) listOf(valueParameter) else emptyList()
        body = irFactory.buildBlockBody(
            listOf(
                if (isSetter) {
                    buildSetField(field, dispatchReceiver, valueParameter.capture())
                } else {
                    buildGetField(field, dispatchReceiver)
                }
            )
        )
        this.returnType = returnType
    }
    return IrFunctionExpressionImpl(
        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
        functionType,
        accessorFunction,
        IrStatementOrigin.LAMBDA
    )
}

internal fun IrCall.getBackingField(): IrField =
    symbol.owner.correspondingPropertySymbol?.let { propertySymbol ->
        propertySymbol.owner.backingField ?: error("Property expected to have backing field")
    } ?: error("Atomic property accessor ${this.render()} expected to have non-null correspondingPropertySymbol")

@OptIn(FirIncompatiblePluginAPI::class)
internal fun IrPluginContext.referencePackageFunction(
    packageName: String,
    name: String,
    predicate: (IrFunctionSymbol) -> Boolean = { true }
): IrSimpleFunctionSymbol = try {
        referenceFunctions(FqName("$packageName.$name")).single(predicate)
    } catch (e: RuntimeException) {
        error("Exception while looking for the function `$name` in package `$packageName`: ${e.message}")
    }

@OptIn(FirIncompatiblePluginAPI::class)
internal fun IrPluginContext.referenceFunction(classSymbol: IrClassSymbol, functionName: String): IrSimpleFunctionSymbol {
    val functionId = FqName("$KOTLIN.${classSymbol.owner.name}.$functionName")
    return try {
        referenceFunctions(functionId).single()
    } catch (e: RuntimeException) {
        error("Exception while looking for the function `$functionId`: ${e.message}")
    }
}

@OptIn(FirIncompatiblePluginAPI::class)
private fun IrPluginContext.referenceArrayClass(irType: IrSimpleType): IrClassSymbol {
    val jsArrayName = irType.getArrayClassFqName()
    return referenceClass(jsArrayName) ?: error("Array class $jsArrayName was not found in the context")
}

@OptIn(FirIncompatiblePluginAPI::class)
internal fun IrPluginContext.getArrayConstructorSymbol(
    irType: IrSimpleType,
    predicate: (IrConstructorSymbol) -> Boolean = { true }
): IrConstructorSymbol {
    val jsArrayName = irType.getArrayClassFqName()
    return try {
        referenceConstructors(jsArrayName).single(predicate)
    } catch (e: RuntimeException) {
        error("Array constructor $jsArrayName matching the predicate was not found in the context")
    }
}

internal fun IrPluginContext.buildPropertyForBackingField(
    field: IrField,
    parent: IrDeclarationContainer,
    visibility: DescriptorVisibility,
    isVar: Boolean,
    isStatic: Boolean
): IrProperty {
    return irFactory.buildProperty {
        name = field.name
        this.visibility = visibility // equal to the atomic property visibility
        this.isVar = isVar
    }.apply {
        backingField = field
        this.parent = parent
        if (!isStatic) {
            updateGetter(field.parent as IrClass, irBuiltIns)
        } else {
            addStaticGetter(irBuiltIns)
        }
        parent.declarations.add(this)
    }
}

internal fun IrProperty.updateGetter(parentContainer: IrDeclarationContainer, irBuiltIns: IrBuiltIns) {
    val property = this
    val field = requireNotNull(backingField) { "BackingField of the property $property should not be null"}
    addGetter {
        origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
        visibility = property.visibility
        returnType = field.type
    }.apply {
        dispatchReceiverParameter = if (parentContainer is IrClass && parentContainer.kind == ClassKind.OBJECT) {
            null
        } else {
            (parentContainer as? IrClass)?.thisReceiver?.deepCopyWithSymbols(this)
        }
        body = factory.createBlockBody(
            UNDEFINED_OFFSET, UNDEFINED_OFFSET, listOf(
                IrReturnImpl(
                    UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                    irBuiltIns.nothingType,
                    symbol,
                    IrGetFieldImpl(
                        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                        field.symbol,
                        field.type,
                        dispatchReceiverParameter?.let {
                            IrGetValueImpl(
                                UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                                it.type,
                                it.symbol
                            )
                        }
                    )
                )
            )
        )
    }
}

internal fun IrProperty.updateSetter(parentClass: IrDeclarationContainer, irBuiltIns: IrBuiltIns) {
    val property = this
    val field = requireNotNull(property.backingField) { "BackingField of the property $property should not be null"}
    addSetter {
        origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
        visibility = property.visibility
        returnType = irBuiltIns.unitType
    }.apply {
        dispatchReceiverParameter = if (parentClass is IrClass && parentClass.kind == ClassKind.OBJECT) {
            null
        } else {
            (parentClass as? IrClass)?.thisReceiver?.deepCopyWithSymbols(this)
        }
        addValueParameter("value", field.type)
        body = factory.createBlockBody(
            UNDEFINED_OFFSET, UNDEFINED_OFFSET, listOf(
                IrReturnImpl(
                    UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                    irBuiltIns.unitType,
                    symbol,
                    IrSetFieldImpl(
                        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                        field.symbol,
                        dispatchReceiverParameter?.let {
                            IrGetValueImpl(
                                UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                                it.type,
                                it.symbol
                            )
                        },
                        this.valueParameters[0].capture(),
                        field.type
                    )
                )
            )
        )
    }
}

internal fun IrProperty.addStaticGetter(irBuiltIns: IrBuiltIns) {
    val field = requireNotNull(backingField) { "BackingField of the property $this should not be null"}
    addGetter {
        origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
        visibility = this@addStaticGetter.visibility
        returnType = field.type
    }.apply {
        dispatchReceiverParameter = null
        body = factory.createBlockBody(
            UNDEFINED_OFFSET, UNDEFINED_OFFSET, listOf(
                IrReturnImpl(
                    UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                    irBuiltIns.nothingType,
                    symbol,
                    IrGetFieldImpl(
                        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                        symbol = field.symbol,
                        type = field.type,
                        receiver = null
                    )
                )
            )
        )
    }
}

internal fun IrPluginContext.buildClassInstance(
    irClass: IrClass,
    parent: IrDeclarationContainer,
    visibility: DescriptorVisibility,
    isStatic: Boolean
): IrProperty =
    buildPropertyForBackingField(
        field = buildClassInstanceField(irClass, parent),
        parent = parent,
        visibility = visibility,
        isVar = false,
        isStatic = isStatic
    )

private fun IrPluginContext.buildClassInstanceField(irClass: IrClass, parent: IrDeclarationContainer) =
    // build a backing field for the wrapper class instance property
    irFactory.buildField {
        this.name = Name.identifier(irClass.name.asString().decapitalizeAsciiOnly())
        type = irClass.defaultType
        isFinal = true
        isStatic = true
        visibility = DescriptorVisibilities.PRIVATE
    }.apply {
        initializer = IrExpressionBodyImpl(
            IrConstructorCallImpl.fromSymbolOwner(
                irClass.defaultType,
                irClass.primaryConstructor!!.symbol
            )
        )
        this.parent = parent
    }

private fun IrSimpleType.getArrayClassFqName(): FqName =
    classifier.signature?.let { signature ->
        signature.getDeclarationNameBySignature().let { name ->
            AFU_ARRAY_CLASSES[name]?.let { jsArrayName ->
                FqName("$KOTLIN.$jsArrayName")
            }
        }
    } ?: error("Unexpected array type ${this.render()}")

internal fun IdSignature.getDeclarationNameBySignature(): String? {
    val commonSignature = if (this is IdSignature.AccessorSignature) accessorSignature else asPublic()
    return commonSignature?.declarationFqName
}

internal fun IrField.isInitializedInInitBlock(parentContainer: IrDeclarationContainer): Boolean {
    for (declaration in parentContainer.declarations) {
        if (declaration is IrAnonymousInitializer) {
            if (declaration.body.statements.any { it is IrSetField && it.symbol == this.symbol }) return true
        }
    }
    return false
}

// reused from: kotlin/compiler/ir/ir.tree/src/org/jetbrains/kotlin/ir/util/IrUtils.kt#remapTypeParameters
// added support of remapping typeParameters in funciton type arguments
internal fun IrType.remapTypeParameters(
    source: IrTypeParametersContainer,
    target: IrTypeParametersContainer
): IrType =
    when (this) {
        is IrSimpleType -> {
            when (val classifier = classifier.owner) {
                is IrTypeParameter -> {
                    val newClassifier =
                        if (classifier.parent == source)
                            target.typeParameters[classifier.index]
                        else
                            classifier
                    IrSimpleTypeImpl(newClassifier.symbol, nullability, arguments, annotations)
                }
                is IrClass -> {
                    val type = IrSimpleTypeImpl(
                        classifier.symbol,
                        nullability,
                        arguments.map {
                            when (it) {
                                is IrSimpleType -> {
                                    val argClassifier = it.classifier.owner
                                    if (argClassifier is IrTypeParameter) {
                                        val newClassifier =
                                            if (argClassifier.parent == source)
                                                target.typeParameters[argClassifier.index]
                                            else
                                                argClassifier
                                        IrSimpleTypeImpl(newClassifier.symbol, it.nullability, it.arguments, it.annotations)
                                    } else it
                                }
                                is IrTypeProjection -> makeTypeProjection(
                                    it.type.remapTypeParameters(source, target),
                                    it.variance
                                )
                                is IrStarProjection -> it
                            }
                        },
                        annotations
                    )
                    type
                }
                else -> this
            }
        }
        else -> this
    }
