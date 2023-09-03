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
      genericExample(input, expected, Base16Lowercase.encode(_), Base16Lowercase.decode(_))

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
      genericExample(input, expected, Base16Uppercase.encode(_), Base16Uppercase.decode(_))

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
      genericExample(input, expected, Base32HexLowercase.encode(_), Base32HexLowercase.decode(_))

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
      genericExample(input, expected, Base32HexUppercase.encode(_), Base32HexUppercase.decode(_))

    example("", "")
    example("f", "CO")
    example("fo", "CPNG")
    example("foo", "CPNMU")
    example("foob", "CPNMUOG")
    example("fooba", "CPNMUOJ1")
    example("foobar", "CPNMUOJ1E8")
  }

  "Base32Lowercase" when {
    def example(input: String, expected: String): Unit =
      genericExample(input, expected, Base32Lowercase.encode(_), Base32Lowercase.decode(_))

    example("", "")
    example("f", "my")
    example("fo", "mzxq")
    example("foo", "mzxw6")
    example("foob", "mzxw6yq")
    example("fooba", "mzxw6ytb")
    example("foobar", "mzxw6ytboi")
  }

  "Base32Uppercase" when {
    def example(input: String, expected: String): Unit =
      genericExample(input, expected, Base32Uppercase.encode(_), Base32Uppercase.decode(_))

    example("", "")
    example("f", "MY")
    example("fo", "MZXQ")
    example("foo", "MZXW6")
    example("foob", "MZXW6YQ")
    example("fooba", "MZXW6YTB")
    example("foobar", "MZXW6YTBOI")
  }

  "Base64" when {
    def example(input: String, expected: String): Unit =
      genericExample(input, expected, Base64.encode(_), Base64.decode(_))

    example("", "")
    example("f", "Zg")
    example("fo", "Zm8")
    example("foo", "Zm9v")
    example("foob", "Zm9vYg")
    example("fooba", "Zm9vYmE")
    example("foobar", "Zm9vYmFy")
  }

  "Base64URLSafe" when {
    def example(input: String, expected: String): Unit =
      genericExample(input, expected, Base64URLSafe.encode(_), Base64URLSafe.decode(_))

    example("", "")
    example("f", "Zg")
    example("fo", "Zm8")
    example("foo", "Zm9v")
    example("foob", "Zm9vYg")
    example("fooba", "Zm9vYmE")
    example("foobar", "Zm9vYmFy")
  }
end BaseNSpec
