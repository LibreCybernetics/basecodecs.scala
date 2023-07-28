package dev.librecybernetics.data

import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class Base16Spec extends AnyWordSpec:
  def genericExample(input: String, expected: String) =
    val encodedLowercase = Base16.encodeLowercase(input)
    val encodedUppercase = Base16.encodeUppercase(input)
    val decodedLowercase = Base16.decodeLowercase(expected.toLowerCase)
    val decodedUppercase = Base16.decodeUppercase(expected.toUpperCase)

    input in {
      encodedLowercase shouldBe expected.toLowerCase
      encodedUppercase shouldBe expected.toUpperCase
      decodedLowercase shouldBe input.getBytes.toSeq
      decodedUppercase shouldBe input.getBytes.toSeq
    }
  end genericExample

  "Base16" when {
    genericExample("", "")
    genericExample("f", "66")
    genericExample("fo", "666F")
    genericExample("foo", "666F6F")
    genericExample("foob", "666F6F62")
    genericExample("fooba", "666F6F6261")
    genericExample("foobar", "666F6F626172")
  }
end Base16Spec
