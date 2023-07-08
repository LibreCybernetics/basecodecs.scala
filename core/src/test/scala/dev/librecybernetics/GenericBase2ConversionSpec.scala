package dev.librecybernetics

import org.scalacheck.Gen
import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.*
import org.scalatest.prop.TableDrivenPropertyChecks.Table

class GenericBase2ConversionSpec extends AnyWordSpec with ScalaCheckPropertyChecks:
  def genericExample(input: String, expected: Map[BasePower, Seq[Byte]]): Assertion =
    val bytes = input.getBytes

    forAll(Table("basePower" -> "encodedValues", expected.toSeq*)) { (base: BasePower, expected: Seq[Byte]) =>
      val encoded = toBase(bytes.toList, base)
      val decoded = String(
        fromBase(expected.toList, base).toArray
      )

      decoded shouldBe input
      encoded shouldBe expected
    }
  end genericExample

  /** Generates a random byte array of length 0-100 with values [0, maxValue]
    *
    * NOTE: Range is inclusive
    *
    * TODO: Move to utils
    */
  def randomByteArray(maxValue: Int): Gen[Seq[Byte]] = for
    length <- Gen.choose(0, 100)
    input  <- Gen.listOfN(length, Gen.choose(0, maxValue).map(_.toByte))
  yield input

  def fromTo(basePower: BasePower): Assertion =
    forAll(randomByteArray(255)) { (input: Seq[Byte]) =>
      val converted = toBase(input, basePower)
      val back      = fromBase(converted, basePower)
      input shouldBe back
    }

  def toFrom(basePower: BasePower, upperValue: Int): Assertion =
    forAll(randomByteArray(upperValue)) { (input: Seq[Byte]) =>
        val converted = fromBase(input, basePower)
        val back      = toBase(converted, basePower)
        input.reverse.dropWhile(_ == 0).reverse shouldBe back.reverse.dropWhile(_ == 0).reverse
    }

  // From RFC 4648 § 10
  "spec test vector" when {
    "empty" in genericExample(
      "",
      Map(
        4 -> Nil,
        5 -> Nil,
        6 -> Nil
      )
    )
    "f" in genericExample(
      "f",
      Map(
        4 -> Seq(6, 6),
        5 -> Seq(12, 24),
        6 -> Seq(25, 32)
      )
    )
    "fo" in genericExample(
      "fo",
      Map(
        4 -> Seq(6, 6, 6, 15),
        5 -> Seq(12, 25, 23, 16),
        6 -> Seq(25, 38, 60)
      )
    )
    "foo" in genericExample(
      "foo",
      Map(
        4 -> Seq(6, 6, 6, 15, 6, 15),
        5 -> Seq(12, 25, 23, 22, 30),
        6 -> Seq(25, 38, 61, 47)
      )
    )
    "foob" in genericExample(
      "foob",
      Map(
        4 -> Seq(6, 6, 6, 15, 6, 15, 6, 2),
        5 -> Seq(12, 25, 23, 22, 30, 24, 16),
        6 -> Seq(25, 38, 61, 47, 24, 32)
      )
    )
    "fooba" in genericExample(
      "fooba",
      Map(
        4 -> Seq(6, 6, 6, 15, 6, 15, 6, 2, 6, 1),
        5 -> Seq(12, 25, 23, 22, 30, 24, 19, 1),
        6 -> Seq(25, 38, 61, 47, 24, 38, 4)
      )
    )
  }

  "from . to = identity" when {
    "base 16" in fromTo(4)
    "base 32" ignore fromTo(5)
    "base 64" ignore fromTo(6)
  }

  "to . from = identity" when {
    "base 16" in toFrom(4, 15)
    "base 32" ignore toFrom(5, 31)
    "base 64" ignore toFrom(6, 63)
  }
