// JVM_JS_DIFFERENCE
package test

annotation class Anno

val property: Int
    @[Anno] get() = 42
