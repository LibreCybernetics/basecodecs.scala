package dev.librecybernetics.data

import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class ImplicitConversionSpec extends AnyWordSpec {
  "implicit conversion" when {
    val byteInput: Array[Byte] = Array(0x01, 0x23)
    val stringInput: String    = "foobar"

    def genericTest[Base](base: BaseConversions[Base]): Assertion = {
      import base.given
      val bytesBase: Base          = byteInput
      val bytesOutput: Array[Byte] = bytesBase
      bytesOutput sameElements byteInput
      val stringBase: Base         = stringInput
      val stringOutput: String     = base.decodeString(stringBase)
      stringOutput shouldBe stringInput
    }

    "base2" in genericTest[Base2](Base2)
    "base8" in genericTest[Base8](Base8)
    "base16Lowercase" in genericTest[Base16Lowercase](Base16Lowercase)
    "base16Uppercase" in genericTest[Base16Uppercase](Base16Uppercase)
  }
}
