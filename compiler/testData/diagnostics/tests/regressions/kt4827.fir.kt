// KT-4827 UOE at PackageType.throwException()
// EA-53605

public interface TestInterface {
}

class C {
    inner class I {

    }
}

fun f() {
    <!INTERFACE_AS_FUNCTION!>TestInterface<!>()
    C.<!RESOLUTION_TO_CLASSIFIER!>I<!>()
}
