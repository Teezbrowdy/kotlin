// JVM_JS_DIFFERENCE
package test

annotation class Anno

class Class {
    val property: Int
        @[Anno] get() = 42
}
