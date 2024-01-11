package dev.librecybernetics.data

import org.scalacheck.Gen
import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.prop.TableDrivenPropertyChecks.Table
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import dev.librecybernetics.data.util.*
import dev.librecybernetics.data.alphabet.RFC4648.base64

class GenericBase2ConversionSpec extends AnyWordSpec with ScalaCheckPropertyChecks:
  def genericExample(input: String, expected: Map[BasePower, Array[Byte]]): Assertion =
    genericExample(input.getBytes, expected)

  def genericExample(input: Array[Byte], expected: Map[BasePower, Array[Byte]]): Assertion =
    forAll(Table("basePower" -> "encodedValues", expected.toSeq*)) { (base: BasePower, expected: Array[Byte]) =>
      val encoded = toBase(input, base, base64)
      val decoded = fromBase(expected.map(base64(_)), base, base64.flip)

      decoded shouldBe input
      encoded shouldBe expected.map(base64(_))
    }
  end genericExample

  def fromTo(basePower: BasePower): Assertion =
    forAll(randomByteArray(255), MinSuccessful(10000)) { (input: Array[Byte]) =>
      val converted = toBase(input, basePower, base64)
      val back      = fromBase(converted, basePower, base64.flip)
      input shouldBe back
    }

  // From RFC 4648 § 10
  "spec test vector" when {
    "empty" in genericExample(
      "",
      Map[BasePower, Array[Byte]](
        4 -> Array.emptyByteArray,
        5 -> Array.emptyByteArray,
        6 -> Array.emptyByteArray
      )
    )
    "f" in genericExample(
      "f",
      Map[BasePower, Array[Byte]](
        4 -> Array(6, 6),
        5 -> Array(12, 24),
        6 -> Array(25, 32)
      )
    )
    "fo" in genericExample(
      "fo",
      Map[BasePower, Array[Byte]](
        4 -> Array(6, 6, 6, 15),
        5 -> Array(12, 25, 23, 16),
        6 -> Array(25, 38, 60)
      )
    )
    "foo" in genericExample(
      "foo",
      Map[BasePower, Array[Byte]](
        4 -> Array(6, 6, 6, 15, 6, 15),
        5 -> Array(12, 25, 23, 22, 30),
        6 -> Array(25, 38, 61, 47)
      )
    )
    "foob" in genericExample(
      "foob",
      Map[BasePower, Array[Byte]](
        4 -> Array(6, 6, 6, 15, 6, 15, 6, 2),
        5 -> Array(12, 25, 23, 22, 30, 24, 16),
        6 -> Array(25, 38, 61, 47, 24, 32)
      )
    )
    "fooba" in genericExample(
      "fooba",
      Map[BasePower, Array[Byte]](
        4 -> Array(6, 6, 6, 15, 6, 15, 6, 2, 6, 1),
        5 -> Array(12, 25, 23, 22, 30, 24, 19, 1),
        6 -> Array(25, 38, 61, 47, 24, 38, 4)
      )
    )
    "jvm complements 1" in genericExample(
      Array[Byte](0, -1),
      Map[BasePower, Array[Byte]](
        4 -> Array(0, 0, 15, 15),
        5 -> Array(0, 3, 31, 16),
        6 -> Array(0, 15, 60)
      )
    )
  }

  "from . to = identity" when {
    "base 2" in fromTo(1)
    "base 8" in fromTo(3)
    "base 16" in fromTo(4)
    "base 32" in fromTo(5)
    "base 64" in fromTo(6)
  }
