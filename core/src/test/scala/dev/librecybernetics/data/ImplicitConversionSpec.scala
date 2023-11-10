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

    "base2" in genericTest(Base2)
    "base8" in genericTest(Base8)
    "base16Lowercase" in genericTest(Base16Lowercase)
    "base16Uppercase" in genericTest(Base16Uppercase)
    "base32HexLowercase" in genericTest(Base32HexLowercase)
    "base32HexUppercase" in genericTest(Base32HexUppercase)
    "base32Lowercase" in genericTest(Base32Lowercase)
    "base32Uppercase" in genericTest(Base32Uppercase)
    "base64" in genericTest(Base64)
    "base64URLSafe" in genericTest(Base64URLSafe)
    "crockfordBase32" in genericTest(CrockfordBase32)
    "zBase32" in genericTest(ZBase32)
  }
}
