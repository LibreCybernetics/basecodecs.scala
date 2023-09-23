package dev.librecybernetics.data

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatestplus.scalacheck.Checkers.check

class AlphabetSpec extends AnyWordSpec with ScalaCheckPropertyChecks:
  "Multibase" should {
    "base2" in MultibaseAlphabets.base2.check(using
      Arbitrary(Gen.choose(0, 1)),
      Arbitrary(Gen.choose('0', '1'))
    )
    "base8" in MultibaseAlphabets.base8.check(using
      Arbitrary(Gen.choose(0, 7)),
      Arbitrary(Gen.choose('0', '7'))
    )
  }

  "RFC4648" should {
    "base16LowerCase" in RFC4648Alphabets.base16Lowercase.check(using
      Arbitrary(Gen.choose(0, 15)),
      Arbitrary(Gen.hexChar)
    )
    "base16UpperCase" in RFC4648Alphabets.base16Uppercase.check(using
        Arbitrary(Gen.choose(0, 15)),
        Arbitrary(Gen.hexChar)
    )
    "base32HexLowerCase" in RFC4648Alphabets.base32HexLowercase.check(using
        Arbitrary(Gen.choose(0, 31)),
        Arbitrary(Gen.oneOf(Gen.numChar, Gen.alphaLowerChar))
    )
    "base32HexUpperCase" in RFC4648Alphabets.base32HexUppercase.check(using
        Arbitrary(Gen.choose(0, 31)),
        Arbitrary(Gen.oneOf(Gen.numChar, Gen.alphaUpperChar))
    )
    "base32LowerCase" in RFC4648Alphabets.base32Lowercase.check(using
        Arbitrary(Gen.choose(0, 31)),
        Arbitrary(Gen.oneOf(Gen.numChar, Gen.alphaLowerChar))
    )
    "base32UpperCase" in RFC4648Alphabets.base32Uppercase.check(using
        Arbitrary(Gen.choose(0, 31)),
        Arbitrary(Gen.oneOf(Gen.numChar, Gen.alphaUpperChar))
    )
    "base64" in RFC4648Alphabets.base64.check(using
        Arbitrary(Gen.choose(0, 63)),
        Arbitrary(Gen.oneOf(Gen.numChar, Gen.alphaLowerChar, Gen.alphaUpperChar, Gen.oneOf('+', '/')))
    )
    "base64Url" in RFC4648Alphabets.base64URLSafe.check(using
        Arbitrary(Gen.choose(0, 63)),
        Arbitrary(Gen.oneOf(Gen.numChar, Gen.alphaLowerChar, Gen.alphaUpperChar, Gen.oneOf('-', '_')))
    )
  }

  "Zooko" should {
    "zBase32" in ZookoAlphabet.zBase32.check(using
        Arbitrary(Gen.choose(0, 31)),
        Arbitrary(Gen.oneOf(Gen.numChar, Gen.alphaLowerChar))
    )
  }
end AlphabetSpec
