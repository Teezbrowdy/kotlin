// FREE_COMPILER_ARGS: -Xprint-files -verbose

import kotlin.test.*
import kotlin.jvm.*

@Test
fun volatileTest() {

}

@Test
fun addition() {
    assertEquals(42, 40 + 2)
}

@Test
fun multiplication () {
    assertEquals(42, 21 * 2)
}

@Test
fun subtraction () {
    assertEquals(42, 50 - 8)
}

@Test
fun division () {
    assertEquals(42, 126 / 3)
}
