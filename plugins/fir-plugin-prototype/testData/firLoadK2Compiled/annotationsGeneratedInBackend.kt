// JVM_JS_DIFFERENCE
// DUMP_KT_IR
package test

import org.jetbrains.kotlin.fir.plugin.AddAnnotations

@AddAnnotations
class Some(val x: Int) {
    fun foo() {}

    class Derived
}
