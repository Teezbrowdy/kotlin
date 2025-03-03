/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.asJava;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.test.generators.GenerateCompilerTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/asJava/lightClasses/lightClassByFqName")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class CompilerLightClassTestGenerated extends AbstractCompilerLightClassTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
    }

    public void testAllFilesPresentInLightClassByFqName() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/lightClassByFqName"), Pattern.compile("^([^.]+)\\.(kt|kts)$"), null, true, "local", "ideRegression");
    }

    @TestMetadata("AnnotatedParameterInEnumConstructor.kt")
    public void testAnnotatedParameterInEnumConstructor() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/AnnotatedParameterInEnumConstructor.kt");
    }

    @TestMetadata("AnnotatedParameterInInnerClassConstructor.kt")
    public void testAnnotatedParameterInInnerClassConstructor() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/AnnotatedParameterInInnerClassConstructor.kt");
    }

    @TestMetadata("AnnotatedPropertyWithSites.kt")
    public void testAnnotatedPropertyWithSites() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/AnnotatedPropertyWithSites.kt");
    }

    @TestMetadata("AnnotationClass.kt")
    public void testAnnotationClass() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/AnnotationClass.kt");
    }

    @TestMetadata("AnnotationJavaRepeatable.kt")
    public void testAnnotationJavaRepeatable() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/AnnotationJavaRepeatable.kt");
    }

    @TestMetadata("AnnotationJvmRepeatable.kt")
    public void testAnnotationJvmRepeatable() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/AnnotationJvmRepeatable.kt");
    }

    @TestMetadata("AnnotationKotlinAndJavaRepeatable.kt")
    public void testAnnotationKotlinAndJavaRepeatable() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/AnnotationKotlinAndJavaRepeatable.kt");
    }

    @TestMetadata("AnnotationKotlinAndJvmRepeatable.kt")
    public void testAnnotationKotlinAndJvmRepeatable() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/AnnotationKotlinAndJvmRepeatable.kt");
    }

    @TestMetadata("AnnotationRepeatable.kt")
    public void testAnnotationRepeatable() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/AnnotationRepeatable.kt");
    }

    @TestMetadata("BackingFields.kt")
    public void testBackingFields() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/BackingFields.kt");
    }

    @TestMetadata("CompanionObject.kt")
    public void testCompanionObject() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/CompanionObject.kt");
    }

    @TestMetadata("Constructors.kt")
    public void testConstructors() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/Constructors.kt");
    }

    @TestMetadata("DataClassWithCustomImplementedMembers.kt")
    public void testDataClassWithCustomImplementedMembers() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/DataClassWithCustomImplementedMembers.kt");
    }

    @TestMetadata("DelegatedNested.kt")
    public void testDelegatedNested() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/DelegatedNested.kt");
    }

    @TestMetadata("Delegation.kt")
    public void testDelegation() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/Delegation.kt");
    }

    @TestMetadata("DeprecatedEnumEntry.kt")
    public void testDeprecatedEnumEntry() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/DeprecatedEnumEntry.kt");
    }

    @TestMetadata("DeprecatedNotHiddenInClass.kt")
    public void testDeprecatedNotHiddenInClass() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/DeprecatedNotHiddenInClass.kt");
    }

    @TestMetadata("DollarsInName.kt")
    public void testDollarsInName() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/DollarsInName.kt");
    }

    @TestMetadata("DollarsInNameNoPackage.kt")
    public void testDollarsInNameNoPackage() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/DollarsInNameNoPackage.kt");
    }

    @TestMetadata("EnumClass.kt")
    public void testEnumClass() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/EnumClass.kt");
    }

    @TestMetadata("EnumClassWithEnumEntries.kt")
    public void testEnumClassWithEnumEntries() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/EnumClassWithEnumEntries.kt");
    }

    @TestMetadata("EnumEntry.kt")
    public void testEnumEntry() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/EnumEntry.kt");
    }

    @TestMetadata("ExtendingInterfaceWithDefaultImpls.kt")
    public void testExtendingInterfaceWithDefaultImpls() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/ExtendingInterfaceWithDefaultImpls.kt");
    }

    @TestMetadata("HiddenDeprecated.kt")
    public void testHiddenDeprecated() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/HiddenDeprecated.kt");
    }

    @TestMetadata("HiddenDeprecatedInClass.kt")
    public void testHiddenDeprecatedInClass() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/HiddenDeprecatedInClass.kt");
    }

    @TestMetadata("InheritingInterfaceDefaultImpls.kt")
    public void testInheritingInterfaceDefaultImpls() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/InheritingInterfaceDefaultImpls.kt");
    }

    @TestMetadata("InlineReified.kt")
    public void testInlineReified() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/InlineReified.kt");
    }

    @TestMetadata("InvalidJavaIdentifierAsAnnotationArgument.kt")
    public void testInvalidJavaIdentifierAsAnnotationArgument() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/InvalidJavaIdentifierAsAnnotationArgument.kt");
    }

    @TestMetadata("InvalidJavaIdentifierAsPropertyInitializer.kt")
    public void testInvalidJavaIdentifierAsPropertyInitializer() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/InvalidJavaIdentifierAsPropertyInitializer.kt");
    }

    @TestMetadata("JavaBetween.kt")
    public void testJavaBetween() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/JavaBetween.kt");
    }

    @TestMetadata("JavaClassWithAnnotation.kt")
    public void testJavaClassWithAnnotation() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/JavaClassWithAnnotation.kt");
    }

    @TestMetadata("JvmNameOnMember.kt")
    public void testJvmNameOnMember() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/JvmNameOnMember.kt");
    }

    @TestMetadata("JvmStatic.kt")
    public void testJvmStatic() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/JvmStatic.kt");
    }

    @TestMetadata("LocalFunctions.kt")
    public void testLocalFunctions() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/LocalFunctions.kt");
    }

    @TestMetadata("NestedObjects.kt")
    public void testNestedObjects() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/NestedObjects.kt");
    }

    @TestMetadata("NonDataClassWithComponentFunctions.kt")
    public void testNonDataClassWithComponentFunctions() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/NonDataClassWithComponentFunctions.kt");
    }

    @TestMetadata("OnlySecondaryConstructors.kt")
    public void testOnlySecondaryConstructors() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/OnlySecondaryConstructors.kt");
    }

    @TestMetadata("PrivateObject.kt")
    public void testPrivateObject() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/PrivateObject.kt");
    }

    @TestMetadata("PublishedApi.kt")
    public void testPublishedApi() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/PublishedApi.kt");
    }

    @TestMetadata("SimpleObject.kt")
    public void testSimpleObject() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/SimpleObject.kt");
    }

    @TestMetadata("SimplePublicField.kt")
    public void testSimplePublicField() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/SimplePublicField.kt");
    }

    @TestMetadata("SpecialAnnotationsOnAnnotationClass.kt")
    public void testSpecialAnnotationsOnAnnotationClass() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/SpecialAnnotationsOnAnnotationClass.kt");
    }

    @TestMetadata("StubOrderForOverloads.kt")
    public void testStubOrderForOverloads() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/StubOrderForOverloads.kt");
    }

    @TestMetadata("SubstitutionOverride.kt")
    public void testSubstitutionOverride() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/SubstitutionOverride.kt");
    }

    @TestMetadata("Throws.kt")
    public void testThrows() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/Throws.kt");
    }

    @TestMetadata("TypePararametersInClass.kt")
    public void testTypePararametersInClass() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/TypePararametersInClass.kt");
    }

    @TestMetadata("VarArgs.kt")
    public void testVarArgs() throws Exception {
        runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/VarArgs.kt");
    }

    @TestMetadata("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class CompilationErrors extends AbstractCompilerLightClassTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        @TestMetadata("ActualClass.kt")
        public void testActualClass() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/ActualClass.kt");
        }

        @TestMetadata("ActualTypeAlias.kt")
        public void testActualTypeAlias() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/ActualTypeAlias.kt");
        }

        @TestMetadata("ActualTypeAliasCustomJvmPackageName.kt")
        public void testActualTypeAliasCustomJvmPackageName() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/ActualTypeAliasCustomJvmPackageName.kt");
        }

        public void testAllFilesPresentInCompilationErrors() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors"), Pattern.compile("^([^.]+)\\.(kt|kts)$"), null, true);
        }

        @TestMetadata("AllInlineOnly.kt")
        public void testAllInlineOnly() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/AllInlineOnly.kt");
        }

        @TestMetadata("AnnotationModifiers.kt")
        public void testAnnotationModifiers() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/AnnotationModifiers.kt");
        }

        @TestMetadata("EnumNameOverride.kt")
        public void testEnumNameOverride() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/EnumNameOverride.kt");
        }

        @TestMetadata("ExpectClass.kt")
        public void testExpectClass() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/ExpectClass.kt");
        }

        @TestMetadata("ExpectObject.kt")
        public void testExpectObject() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/ExpectObject.kt");
        }

        @TestMetadata("ExpectedNestedClass.kt")
        public void testExpectedNestedClass() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/ExpectedNestedClass.kt");
        }

        @TestMetadata("ExpectedNestedClassInObject.kt")
        public void testExpectedNestedClassInObject() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/ExpectedNestedClassInObject.kt");
        }

        @TestMetadata("FunctionWithoutName.kt")
        public void testFunctionWithoutName() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/FunctionWithoutName.kt");
        }

        @TestMetadata("JvmPackageName.kt")
        public void testJvmPackageName() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/JvmPackageName.kt");
        }

        @TestMetadata("LocalInAnnotation.kt")
        public void testLocalInAnnotation() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/LocalInAnnotation.kt");
        }

        @TestMetadata("PrivateInTrait.kt")
        public void testPrivateInTrait() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/PrivateInTrait.kt");
        }

        @TestMetadata("PropertyWithoutName.kt")
        public void testPropertyWithoutName() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/PropertyWithoutName.kt");
        }

        @TestMetadata("RepetableAnnotations.kt")
        public void testRepetableAnnotations() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/RepetableAnnotations.kt");
        }

        @TestMetadata("SameName.kt")
        public void testSameName() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/SameName.kt");
        }

        @TestMetadata("TopLevelDestructuring.kt")
        public void testTopLevelDestructuring() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/TopLevelDestructuring.kt");
        }

        @TestMetadata("TraitClassObjectField.kt")
        public void testTraitClassObjectField() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/TraitClassObjectField.kt");
        }

        @TestMetadata("TwoOverrides.kt")
        public void testTwoOverrides() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/TwoOverrides.kt");
        }

        @TestMetadata("unresolvedQuialifierInAnnotation.kt")
        public void testUnresolvedQuialifierInAnnotation() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/unresolvedQuialifierInAnnotation.kt");
        }

        @TestMetadata("WrongAnnotations.kt")
        public void testWrongAnnotations() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/compilationErrors/WrongAnnotations.kt");
        }
    }

    @TestMetadata("compiler/testData/asJava/lightClasses/lightClassByFqName/delegation")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Delegation extends AbstractCompilerLightClassTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInDelegation() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/lightClassByFqName/delegation"), Pattern.compile("^([^.]+)\\.(kt|kts)$"), null, true);
        }

        @TestMetadata("Function.kt")
        public void testFunction() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/delegation/Function.kt");
        }

        @TestMetadata("Property.kt")
        public void testProperty() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/delegation/Property.kt");
        }

        @TestMetadata("WithImplicitType.kt")
        public void testWithImplicitType() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/delegation/WithImplicitType.kt");
        }
    }

    @TestMetadata("compiler/testData/asJava/lightClasses/lightClassByFqName/facades")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Facades extends AbstractCompilerLightClassTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInFacades() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/lightClassByFqName/facades"), Pattern.compile("^([^.]+)\\.(kt|kts)$"), null, true);
        }

        @TestMetadata("AllPrivate.kt")
        public void testAllPrivate() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/facades/AllPrivate.kt");
        }

        @TestMetadata("InternalFacadeClass.kt")
        public void testInternalFacadeClass() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/facades/InternalFacadeClass.kt");
        }

        @TestMetadata("MultiFile.kt")
        public void testMultiFile() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/facades/MultiFile.kt");
        }

        @TestMetadata("SingleFile.kt")
        public void testSingleFile() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/facades/SingleFile.kt");
        }

        @TestMetadata("SingleJvmClassName.kt")
        public void testSingleJvmClassName() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/facades/SingleJvmClassName.kt");
        }
    }

    @TestMetadata("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class NullabilityAnnotations extends AbstractCompilerLightClassTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInNullabilityAnnotations() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations"), Pattern.compile("^([^.]+)\\.(kt|kts)$"), null, true);
        }

        @TestMetadata("Class.kt")
        public void testClass() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/Class.kt");
        }

        @TestMetadata("ClassObjectField.kt")
        public void testClassObjectField() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/ClassObjectField.kt");
        }

        @TestMetadata("ClassWithConstructor.kt")
        public void testClassWithConstructor() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/ClassWithConstructor.kt");
        }

        @TestMetadata("ClassWithConstructorAndProperties.kt")
        public void testClassWithConstructorAndProperties() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/ClassWithConstructorAndProperties.kt");
        }

        @TestMetadata("FileFacade.kt")
        public void testFileFacade() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/FileFacade.kt");
        }

        @TestMetadata("Generic.kt")
        public void testGeneric() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/Generic.kt");
        }

        @TestMetadata("ImplicitArrayWithFlexibleParameterTypes.kt")
        public void testImplicitArrayWithFlexibleParameterTypes() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/ImplicitArrayWithFlexibleParameterTypes.kt");
        }

        @TestMetadata("IntOverridesAny.kt")
        public void testIntOverridesAny() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/IntOverridesAny.kt");
        }

        @TestMetadata("JvmOverloads.kt")
        public void testJvmOverloads() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/JvmOverloads.kt");
        }

        @TestMetadata("NullableUnitReturn.kt")
        public void testNullableUnitReturn() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/NullableUnitReturn.kt");
        }

        @TestMetadata("OverrideAnyWithUnit.kt")
        public void testOverrideAnyWithUnit() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/OverrideAnyWithUnit.kt");
        }

        @TestMetadata("PlatformTypes.kt")
        public void testPlatformTypes() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/PlatformTypes.kt");
        }

        @TestMetadata("PrimitiveBackedInlineClasses.kt")
        public void testPrimitiveBackedInlineClasses() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/PrimitiveBackedInlineClasses.kt");
        }

        @TestMetadata("Primitives.kt")
        public void testPrimitives() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/Primitives.kt");
        }

        @TestMetadata("PrivateInClass.kt")
        public void testPrivateInClass() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/PrivateInClass.kt");
        }

        @TestMetadata("Synthetic.kt")
        public void testSynthetic() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/Synthetic.kt");
        }

        @TestMetadata("Trait.kt")
        public void testTrait() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/Trait.kt");
        }

        @TestMetadata("UnitAsGenericArgument.kt")
        public void testUnitAsGenericArgument() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/UnitAsGenericArgument.kt");
        }

        @TestMetadata("UnitParameter.kt")
        public void testUnitParameter() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/UnitParameter.kt");
        }

        @TestMetadata("VoidReturn.kt")
        public void testVoidReturn() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/nullabilityAnnotations/VoidReturn.kt");
        }
    }

    @TestMetadata("compiler/testData/asJava/lightClasses/lightClassByFqName/script")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Script extends AbstractCompilerLightClassTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInScript() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/lightClassByFqName/script"), Pattern.compile("^([^.]+)\\.(kt|kts)$"), null, true);
        }

        @TestMetadata("HelloWorld.kts")
        public void testHelloWorld() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/script/HelloWorld.kts");
        }

        @TestMetadata("InnerClasses.kts")
        public void testInnerClasses() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/script/InnerClasses.kts");
        }
    }

    @TestMetadata("compiler/testData/asJava/lightClasses/lightClassByFqName/withTestCompilerPluginEnabled")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class WithTestCompilerPluginEnabled extends AbstractCompilerLightClassTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInWithTestCompilerPluginEnabled() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/asJava/lightClasses/lightClassByFqName/withTestCompilerPluginEnabled"), Pattern.compile("^([^.]+)\\.(kt|kts)$"), null, true);
        }

        @TestMetadata("allOpen.kt")
        public void testAllOpen() throws Exception {
            runTest("compiler/testData/asJava/lightClasses/lightClassByFqName/withTestCompilerPluginEnabled/allOpen.kt");
        }
    }
}
