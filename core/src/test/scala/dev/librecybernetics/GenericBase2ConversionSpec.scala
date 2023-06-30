package dev.librecybernetics

import org.scalacheck.{Gen, *}
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.*

class GenericBase2ConversionSpec extends AnyWordSpec with ScalaCheckPropertyChecks:
  // TODO: Move to shared utils
  extension (b: Byte) def toHexString: String = b.toInt.toHexString.takeRight(2)
  end extension

  // TODO: Move to shared utils
  extension (s: Seq[Byte]) def toHexString: String = s.map(_.toHexString).mkString("_")
  end extension

  /** Generates a random byte array of length 0-100 with values [0, maxValue]
    *
    * NOTE: Range is inclusive
    */
  def randomByteArray(maxValue: Int): Gen[Seq[Byte]] = for
    length <- Gen.choose(0, 100)
    input  <- Gen.listOfN(length, Gen.choose(0, maxValue).map(_.toByte))
  yield input

  "from . to = identity" when {
    "base 16}" in {
      forAll(randomByteArray(255)) { (input: Seq[Byte]) =>
        val converted = toBase(input, 4)
        val back      = fromBase(converted, 4)
        input shouldBe back
      }
    }
  }

  "to . from = identity" when {
    "base 16" in {
      forAll(randomByteArray(15)) { (input: Seq[Byte]) =>
        // NOTE: Both 0x0 and 0x00 decode to the same thing, so we drop trailing 0s
        // TODO: Check spec for how to decode the previous example
        whenever(!input.reverse.headOption.contains(0)) {
          val converted = fromBase(input, 4)
          val back      = toBase(converted, 4)
          input shouldBe back.reverse.dropWhile(_ == 0).reverse
        }
      }
    }
  }
