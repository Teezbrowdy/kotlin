/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the LICENSE file.
 */

package kotlin.native.internal.test

import kotlin.experimental.ExperimentalNativeApi
import kotlin.IllegalArgumentException
import kotlin.system.getTimeMillis
import kotlin.system.measureTimeMillis

/**
 * Represents a test case, that is a test method marked with `@Test` annotaion.
 *
 * @see kotlin.test.Test
 */
@ExperimentalNativeApi
public interface TestCase {
    val name: String

    /**
     * Shows if this test is ignored by the `@Ignore` annotation.
     *
     * @see kotlin.test.Ignore
     */
    val ignored: Boolean

    /**
     * Test Suite the test belong to.
     */
    val suite: TestSuite

    /**
     * Runs all methods that were marked with `@BeforeTest` annotation.
     *
     * @see kotlin.test.BeforeTest
     */
    fun doBefore()

    /**
     * Runs the test method itself
     *
     * @see kotlin.test.Test
     */
    fun doRun()

    /**
     * Runs test with its before and after functions.
     */
    fun run() {
        try {
            doBefore()
            doRun()
        } finally {
            // TODO: in case of an incorrect setup in doBefore() will doAfter() hide it by throwing a new exception
            //  when it will try to cleanup something that wasn't initialized?
            doAfter()
        }
    }

    /**
     * Runs all methods that were marked with `@AfterTest` annotation.
     *
     * @see kotlin.test.AfterTest
     */
    fun doAfter()
}

@ExperimentalNativeApi
internal val TestCase.prettyName get() = "${suite.name}.$name"

@ExperimentalNativeApi
public interface TestSuite {
    val name: String
    val ignored: Boolean
    val testCases: Map<String, TestCase>
    val size : Int

    fun doBeforeClass()
    fun doAfterClass()
}

@ExperimentalNativeApi
public enum class TestFunctionKind {
    BEFORE_TEST,
    AFTER_TEST,
    BEFORE_CLASS,
    AFTER_CLASS
}

@ExperimentalNativeApi
public abstract class AbstractTestSuite<F: Function<Unit>>(override val name: String, override val ignored: Boolean)
    : TestSuite {
    override fun toString(): String = name

    // TODO: Make inner and remove the type param when the bug is fixed.
    abstract class BasicTestCase<F: Function<Unit>>(
            override val name: String,
            override val suite: AbstractTestSuite<F>,
            val testFunction: F,
            override val ignored: Boolean
    ) : TestCase {
        override fun toString(): String = "$name ($suite)"
    }

    private val _testCases = mutableMapOf<String, BasicTestCase<F>>()
    override val testCases: Map<String, BasicTestCase<F>>
        get() = _testCases

    private fun registerTestCase(testCase: BasicTestCase<F>) = _testCases.put(testCase.name, testCase)

    fun registerTestCase(name: String, testFunction: F, ignored: Boolean) =
            registerTestCase(createTestCase(name, testFunction, ignored))

    abstract fun createTestCase(name: String, testFunction: F, ignored: Boolean):  BasicTestCase<F>

    init {
        registerSuite(this)
    }

    override val size: Int
        get() = testCases.size
}

@ExperimentalNativeApi
public abstract class BaseClassSuite<INSTANCE, COMPANION>(name: String, ignored: Boolean)
    : AbstractTestSuite<INSTANCE.() -> Unit>(name, ignored) {

    class TestCase<INSTANCE, COMPANION>(name: String,
                                        override val suite: BaseClassSuite<INSTANCE, COMPANION>,
                                        testFunction: INSTANCE.() -> Unit,
                                        ignored: Boolean)
        : BasicTestCase<INSTANCE.() -> Unit>(name, suite, testFunction, ignored) {

        internal val instance: INSTANCE by lazy { suite.createInstance() }

        override fun doBefore() {
            suite.before.forEach { instance.it() }
        }

        override fun doAfter() {
            suite.after.forEach { instance.it() }
        }

        override fun doRun() {
            instance.testFunction()
        }
    }

    // These two methods are overridden in test suite classes generated by the compiler.
    abstract fun createInstance(): INSTANCE
    open fun getCompanion(): COMPANION = throw NotImplementedError("Test class has no companion object")

    companion object {
        val INSTANCE_KINDS = listOf(TestFunctionKind.BEFORE_TEST, TestFunctionKind.AFTER_TEST)
        val COMPANION_KINDS = listOf(TestFunctionKind.BEFORE_CLASS, TestFunctionKind.AFTER_CLASS)
    }

    private val instanceFunctions = mutableMapOf<TestFunctionKind, MutableSet<INSTANCE.() -> Unit>>()
    private fun getInstanceFunctions(kind: TestFunctionKind): MutableCollection<INSTANCE.() -> Unit> {
        check(kind in INSTANCE_KINDS)
        return instanceFunctions.getOrPut(kind) { mutableSetOf() }
    }

    private val companionFunction = mutableMapOf<TestFunctionKind, MutableSet<COMPANION.() -> Unit>>()
    private fun getCompanionFunctions(kind: TestFunctionKind): MutableCollection<COMPANION.() -> Unit> {
        check(kind in COMPANION_KINDS)
        return companionFunction.getOrPut(kind) { mutableSetOf() }
    }

    val before:      Collection<INSTANCE.() -> Unit>  get() = getInstanceFunctions(TestFunctionKind.BEFORE_TEST)
    val after:       Collection<INSTANCE.() -> Unit>  get() = getInstanceFunctions(TestFunctionKind.AFTER_TEST)

    val beforeClass: Collection<COMPANION.() -> Unit>  get() = getCompanionFunctions(TestFunctionKind.BEFORE_CLASS)
    val afterClass:  Collection<COMPANION.() -> Unit>  get() = getCompanionFunctions(TestFunctionKind.AFTER_CLASS)

    @Suppress("UNCHECKED_CAST")
    fun registerFunction(kind: TestFunctionKind, function: Function1<*, Unit>) =
            when (kind) {
                in INSTANCE_KINDS -> getInstanceFunctions(kind).add(function as INSTANCE.() -> Unit)
                in COMPANION_KINDS -> getCompanionFunctions(kind).add(function as COMPANION.() -> Unit)
                else -> throw IllegalArgumentException("Unknown function kind: $kind")
            }

    override fun doBeforeClass() = beforeClass.forEach { getCompanion().it() }
    override fun doAfterClass() = afterClass.forEach { getCompanion().it() }

    override fun createTestCase(name: String, testFunction: INSTANCE.() -> Unit, ignored: Boolean)
            : BasicTestCase<INSTANCE.() -> Unit> =
            TestCase<INSTANCE, COMPANION>(name, this, testFunction, ignored)
}

private typealias TopLevelFun = () -> Unit

@ExperimentalNativeApi
public class TopLevelSuite(name: String): AbstractTestSuite<TopLevelFun>(name, false) {

    class TestCase(name: String, override val suite: TopLevelSuite, testFunction: TopLevelFun, ignored: Boolean)
        : BasicTestCase<TopLevelFun>(name, suite, testFunction, ignored) {
        override fun doBefore() {
            suite.before.forEach { it() }
        }

        override fun doAfter() {
            suite.after.forEach { it() }
        }

        override fun doRun() {
            testFunction()
        }
    }

    private val specialFunctions = mutableMapOf<TestFunctionKind, MutableSet<TopLevelFun>>()
    private fun getFunctions(type: TestFunctionKind) = specialFunctions.getOrPut(type) { mutableSetOf() }

    val before:      Collection<TopLevelFun>  get() = getFunctions(TestFunctionKind.BEFORE_TEST)
    val after:       Collection<TopLevelFun>  get() = getFunctions(TestFunctionKind.AFTER_TEST)
    val beforeClass: Collection<TopLevelFun>  get() = getFunctions(TestFunctionKind.BEFORE_CLASS)
    val afterClass:  Collection<TopLevelFun>  get() = getFunctions(TestFunctionKind.AFTER_CLASS)

    fun registerFunction(kind: TestFunctionKind, function: TopLevelFun) = getFunctions(kind).add(function)

    override fun doBeforeClass() = beforeClass.forEach { it() }
    override fun doAfterClass() = afterClass.forEach { it() }

    override fun createTestCase(name: String, testFunction: TopLevelFun, ignored: Boolean)
            : BasicTestCase<TopLevelFun> = TestCase(name, this, testFunction, ignored)
}