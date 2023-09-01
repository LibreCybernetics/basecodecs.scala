package dev.librecybernetics.data

import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class BaseNSpec extends AnyWordSpec:
  def genericExample(
    input: String,
    expected: String,
    encode: String => String,
    decode: String => Array[Byte]
  ): Unit =
    val encoded = encode(input)
    val decoded = decode(expected)

    input in {
      encoded shouldBe expected
      decoded shouldBe input.getBytes
    }
  end genericExample

  "Base16Lowercase" when {
    def example(input: String, expected: String): Unit =
      genericExample(input, expected, Base16Lowercase.encode, Base16Lowercase.decode)

    example("", "")
    example("f", "66")
    example("fo", "666f")
    example("foo", "666f6f")
    example("foob", "666f6f62")
    example("fooba", "666f6f6261")
    example("foobar", "666f6f626172")
  }

  "Base16Uppercase" when {
    def example(input: String, expected: String): Unit =
      genericExample(input, expected, Base16Uppercase.encode, Base16Uppercase.decode)

    example("", "")
    example("f", "66")
    example("fo", "666F")
    example("foo", "666F6F")
    example("foob", "666F6F62")
    example("fooba", "666F6F6261")
    example("foobar", "666F6F626172")
  }

  "Base32HexLowercase" when {
    def example(input: String, expected: String): Unit =
      genericExample(input, expected, Base32HexLowercase.encode, Base32HexLowercase.decode)

    example("", "")
    example("f", "co")
    example("fo", "cpng")
    example("foo", "cpnmu")
    example("foob", "cpnmuog")
    example("fooba", "cpnmuoj1")
    example("foobar", "cpnmuoj1e8")
  }

  "Base32HexUppercase" when {
    def example(input: String, expected: String): Unit =
      genericExample(input, expected, Base32HexUppercase.encode, Base32HexUppercase.decode)

    example("", "")
    example("f", "CO")
    example("fo", "CPNG")
    example("foo", "CPNMU")
    example("foob", "CPNMUOG")
    example("fooba", "CPNMUOJ1")
    example("foobar", "CPNMUOJ1E8")
  }
end BaseNSpec
