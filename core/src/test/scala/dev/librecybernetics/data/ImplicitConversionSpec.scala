package dev.librecybernetics.data

import org.scalatest.wordspec.AnyWordSpec

class ImplicitConversionSpec extends AnyWordSpec {
  "implicit conversion" when {
    val byteInput: Array[Byte] = Array(0x01, 0x23)
    val stringInput: String = "foobar"

    "base2" in {
      val byteBase2: Base2 = byteInput
      val byteOutput: Array[Byte] = byteBase2
      val stringBase2: Base2 = stringInput
      val stringOutput: String = Base2.given_Conversion_Base2_String(stringBase2)
    }

    "base8" in {
      val byteBase8: Base8 = byteInput
      val byteOutput: Array[Byte] = byteBase8
      val stringBase8: Base8 = stringInput
      val stringOutput: String = Base8.given_Conversion_Base8_String(stringBase8)
    }
  }
}
