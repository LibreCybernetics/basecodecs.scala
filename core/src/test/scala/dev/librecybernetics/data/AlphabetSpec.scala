package dev.librecybernetics.data

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks.*
import org.scalatestplus.scalacheck.Checkers.check

import dev.librecybernetics.data.alphabet.{Crockford, Multibase, RFC4648, Zooko}

class AlphabetSpec extends AnyWordSpec:
  given Arbitrary[Char] = Arbitrary(Gen.choose(32.toChar, 126.toChar))
  given Arbitrary[Byte] = Arbitrary(Gen.choose(-127.toByte, 127.toByte))

  "Multibase" should {
    "base2" in Multibase.base2.check(MinSuccessful(100), MaxDiscardedFactor(1000.0))
    "base2 error" in {
      a[MatchError] should be thrownBy Multibase.base2.apply(-1)
      a[MatchError] should be thrownBy Multibase.base2.apply(2)
      a[MatchError] should be thrownBy Multibase.base2.reverse('/')
      a[MatchError] should be thrownBy Multibase.base2.reverse('2')
    }
    "base8" in Multibase.base8.check(MinSuccessful(100), MaxDiscardedFactor(1000.0))
    "base8 error" in {
      a[MatchError] should be thrownBy Multibase.base8.apply(-1)
      a[MatchError] should be thrownBy Multibase.base8.apply(8)
      a[MatchError] should be thrownBy Multibase.base8.reverse('/')
      a[MatchError] should be thrownBy Multibase.base8.reverse('8')
    }
  }

  "RFC4648" should {
    "base16LowerCase" in RFC4648.base16Lowercase.check(MinSuccessful(1000), MaxDiscardedFactor(100.0))
    "base16LowerCase error" in {
      a[MatchError] should be thrownBy RFC4648.base16Lowercase.apply(-1)
      a[MatchError] should be thrownBy RFC4648.base16Lowercase.apply(17)
      a[MatchError] should be thrownBy RFC4648.base16Lowercase.reverse('/')
      a[MatchError] should be thrownBy RFC4648.base16Lowercase.reverse(':')
      a[MatchError] should be thrownBy RFC4648.base16Lowercase.reverse('`')
      a[MatchError] should be thrownBy RFC4648.base16Lowercase.reverse('g')
    }
    "base16UpperCase" in RFC4648.base16Uppercase.check(MinSuccessful(1000), MaxDiscardedFactor(100.0))
    "base16UpperCase error" in {
      a[MatchError] should be thrownBy RFC4648.base16Uppercase.apply(-1)
      a[MatchError] should be thrownBy RFC4648.base16Uppercase.apply(17)
      a[MatchError] should be thrownBy RFC4648.base16Uppercase.reverse('/')
      a[MatchError] should be thrownBy RFC4648.base16Uppercase.reverse(':')
      a[MatchError] should be thrownBy RFC4648.base16Uppercase.reverse('@')
      a[MatchError] should be thrownBy RFC4648.base16Uppercase.reverse('G')
    }
    "base32HexLowerCase" in RFC4648.base32HexLowercase.check(MinSuccessful(1000), MaxDiscardedFactor(100.0))
    "base32HexLowerCase error" in {
      a[MatchError] should be thrownBy RFC4648.base32HexLowercase.apply(-1)
      a[MatchError] should be thrownBy RFC4648.base32HexLowercase.apply(32)
      a[MatchError] should be thrownBy RFC4648.base32HexLowercase.reverse('/')
      a[MatchError] should be thrownBy RFC4648.base32HexLowercase.reverse(':')
      a[MatchError] should be thrownBy RFC4648.base32HexLowercase.reverse('`')
      a[MatchError] should be thrownBy RFC4648.base32HexLowercase.reverse('w')
    }
    "base32HexUpperCase" in RFC4648.base32HexUppercase.check(MinSuccessful(1000), MaxDiscardedFactor(100.0))
    "base32HexUpperCase error" in {
      a[MatchError] should be thrownBy RFC4648.base32HexUppercase.apply(-1)
      a[MatchError] should be thrownBy RFC4648.base32HexUppercase.apply(32)
      a[MatchError] should be thrownBy RFC4648.base32HexUppercase.reverse('/')
      a[MatchError] should be thrownBy RFC4648.base32HexUppercase.reverse(':')
      a[MatchError] should be thrownBy RFC4648.base32HexUppercase.reverse('@')
      a[MatchError] should be thrownBy RFC4648.base32HexUppercase.reverse('W')
    }
    "base32LowerCase" in RFC4648.base32Lowercase.check(MinSuccessful(1000), MaxDiscardedFactor(100.0))
    "base32LowerCase error" in {
      a[MatchError] should be thrownBy RFC4648.base32Lowercase.apply(-1)
      a[MatchError] should be thrownBy RFC4648.base32Lowercase.apply(32)
      a[MatchError] should be thrownBy RFC4648.base32Lowercase.reverse('`')
      a[MatchError] should be thrownBy RFC4648.base32Lowercase.reverse('{')
      a[MatchError] should be thrownBy RFC4648.base32Lowercase.reverse('1')
      a[MatchError] should be thrownBy RFC4648.base32Lowercase.reverse('8')
    }
    "base32UpperCase" in RFC4648.base32Uppercase.check(MinSuccessful(1000), MaxDiscardedFactor(100.0))
    "base32UpperCase error" in {
      a[MatchError] should be thrownBy RFC4648.base32Uppercase.apply(-1)
      a[MatchError] should be thrownBy RFC4648.base32Uppercase.apply(32)
      a[MatchError] should be thrownBy RFC4648.base32Uppercase.reverse('@')
      a[MatchError] should be thrownBy RFC4648.base32Uppercase.reverse('[')
      a[MatchError] should be thrownBy RFC4648.base32Uppercase.reverse('1')
      a[MatchError] should be thrownBy RFC4648.base32Uppercase.reverse('8')
    }
    "base64" in RFC4648.base64.check(MinSuccessful(1000), MaxDiscardedFactor(100.0))
    "base64 error" in {
      a[MatchError] should be thrownBy RFC4648.base64.apply(-1)
      a[MatchError] should be thrownBy RFC4648.base64.apply(64)
      a[MatchError] should be thrownBy RFC4648.base64.reverse('.')
      a[MatchError] should be thrownBy RFC4648.base64.reverse(':')
      a[MatchError] should be thrownBy RFC4648.base64.reverse('@')
      a[MatchError] should be thrownBy RFC4648.base64.reverse('[')
      a[MatchError] should be thrownBy RFC4648.base64.reverse('`')
      a[MatchError] should be thrownBy RFC4648.base64.reverse('{')
    }
    "base64Url" in RFC4648.base64URLSafe.check(MinSuccessful(1000), MaxDiscardedFactor(100.0))
    "base64Url error" in {
      a[MatchError] should be thrownBy RFC4648.base64URLSafe.apply(-1)
      a[MatchError] should be thrownBy RFC4648.base64URLSafe.apply(64)
      a[MatchError] should be thrownBy RFC4648.base64URLSafe.reverse('/')
      a[MatchError] should be thrownBy RFC4648.base64URLSafe.reverse(':')
      a[MatchError] should be thrownBy RFC4648.base64URLSafe.reverse('@')
      a[MatchError] should be thrownBy RFC4648.base64URLSafe.reverse('[')
      a[MatchError] should be thrownBy RFC4648.base64URLSafe.reverse('`')
      a[MatchError] should be thrownBy RFC4648.base64URLSafe.reverse('{')
    }
  }

  "Crockford" should {
    "crockfordBase32" in Crockford.base32.checkForward(MinSuccessful(1000), MaxDiscardedFactor(100.0))
    "crockfordBase32 error" in {
      a[MatchError] should be thrownBy Crockford.base32.apply(-1)
      a[MatchError] should be thrownBy Crockford.base32.apply(32)
    }
  }

  "Zooko" should {
    "zBase32" in Zooko.base32.check(MinSuccessful(1000), MaxDiscardedFactor(100.0))
    "zBase32 error" in {
      a[MatchError] should be thrownBy Zooko.base32.apply(-1)
      a[MatchError] should be thrownBy Zooko.base32.apply(32)
    }
  }
end AlphabetSpec
