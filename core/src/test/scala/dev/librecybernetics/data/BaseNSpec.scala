package dev.librecybernetics.data

import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.prop.TableDrivenPropertyChecks.Table
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import dev.librecybernetics.data.codec.{
  Base16Lowercase,
  Base16Uppercase,
  Base2,
  Base32HexLowercase,
  Base32HexUppercase,
  Base32Lowercase,
  Base32Uppercase,
  Base64,
  Base64URLSafe,
  Base8,
  CrockfordBase32,
  GenericCodec,
  ZBase32
}

/**   - Test cases related to foobar were taken from RFC4648§10 (https://tools.ietf.org/html/rfc4648)
  *   - Test cases related to {incrementing,decrementing}ByteArray were checked against https://cryptii.com/ (Except
  *     base8)
  */
class BaseNSpec extends AnyWordSpec with ScalaCheckPropertyChecks:
  private val incrementingByteArray: Array[Byte] = Array[Byte](
    0x01,
    0x23,
    0x45,
    0x67,
    0x89.toByte,
    0xab.toByte,
    0xcd.toByte,
    0xef.toByte
  )
  private val decrementingByteArray: Array[Byte] = Array[Byte](
    0xfe.toByte,
    0xdc.toByte,
    0xba.toByte,
    0x98.toByte,
    0x76,
    0x54,
    0x32,
    0x10
  )

  def genericTest(
      codec: GenericCodec
  )(
      examples: (Array[Byte] | String, String)*
  ): Assertion =
    forAll(Table("Input" -> "Encoded", examples*)) { (input, expected) =>
      val encoded        = codec.encode(input)
      val Right(decoded) = codec.decode(expected): @unchecked

      encoded shouldBe expected

      input match
        case byteArray: Array[Byte] => decoded shouldBe byteArray
        case string: String         => decoded shouldBe string.getBytes
      end match
    }

    // Property-check on arbitrary byte arrays
    forAll(randomByteArray(255), MinSuccessful(10000)) { input =>
      val encoded        = codec.encode(input)
      val Right(decoded) = codec.decode(encoded): @unchecked

      decoded shouldBe input
    }

    // Property-check on arbitrary strings
    forAll(MinSuccessful(10000)) { (input: String) =>
      val encoded        = codec.encode(input)
      val Right(decoded) = codec.decodeString(encoded): @unchecked

      decoded shouldBe input
    }
  end genericTest

  "Base2" in genericTest(Base2)(
    // Strings
    ""       -> "",
    "f"      -> "01100110",
    "fo"     -> "0110011001101111",
    "foo"    -> "011001100110111101101111",
    "foob"   -> "01100110011011110110111101100010",
    "fooba"  -> "0110011001101111011011110110001001100001",
    "foobar" -> "011001100110111101101111011000100110000101110010",

    // Byte arrays
    Array[Byte](0x0)      -> "00000000",
    incrementingByteArray -> "0000000100100011010001010110011110001001101010111100110111101111",
    decrementingByteArray -> "1111111011011100101110101001100001110110010101000011001000010000"
  )

  "Base8" in genericTest(Base8)(
    // Strings
    ""       -> "",
    "f"      -> "314",
    "fo"     -> "314674",
    "foo"    -> "31467557=",
    "foob"   -> "31467557304=",
    "fooba"  -> "31467557304604=",
    "foobar" -> "3146755730460562==",

    // Byte arrays
    Array[Byte](0x0)      -> "000",
    incrementingByteArray -> "0022150531704653633674==",
    decrementingByteArray -> "7755627246073124144100=="
  )

  "Base16Lowercase" in genericTest(Base16Lowercase)(
    // Strings
    ""       -> "",
    "f"      -> "66",
    "fo"     -> "666f",
    "foo"    -> "666f6f",
    "foob"   -> "666f6f62",
    "fooba"  -> "666f6f6261",
    "foobar" -> "666f6f626172",

    // Byte arrays
    Array[Byte](0x0)      -> "00",
    incrementingByteArray -> "0123456789abcdef",
    decrementingByteArray -> "fedcba9876543210"
  )

  "Base16Uppercase" in genericTest(Base16Uppercase)(
    // Strings
    ""       -> "",
    "f"      -> "66",
    "fo"     -> "666F",
    "foo"    -> "666F6F",
    "foob"   -> "666F6F62",
    "fooba"  -> "666F6F6261",
    "foobar" -> "666F6F626172",

    // Byte arrays
    Array[Byte](0x0)      -> "00",
    incrementingByteArray -> "0123456789ABCDEF",
    decrementingByteArray -> "FEDCBA9876543210"
  )

  "Base32HexLowercase" in genericTest(Base32HexLowercase)(
    // Strings
    ""       -> "",
    "f"      -> "co======",
    "fo"     -> "cpng====",
    "foo"    -> "cpnmu===",
    "foob"   -> "cpnmuog=",
    "fooba"  -> "cpnmuoj1",
    "foobar" -> "cpnmuoj1e8======",

    // Byte arrays
    Array[Byte](0x0)      -> "00======",
    incrementingByteArray -> "04hkaps9lf6uu===",
    decrementingByteArray -> "vrebl63magp10==="
  )

  "Base32HexUppercase" in genericTest(Base32HexUppercase)(
    // Strings
    ""       -> "",
    "f"      -> "CO======",
    "fo"     -> "CPNG====",
    "foo"    -> "CPNMU===",
    "foob"   -> "CPNMUOG=",
    "fooba"  -> "CPNMUOJ1",
    "foobar" -> "CPNMUOJ1E8======",

    // Byte arrays
    Array[Byte](0x0)      -> "00======",
    incrementingByteArray -> "04HKAPS9LF6UU===",
    decrementingByteArray -> "VREBL63MAGP10==="
  )

  "Base32Lowercase" in genericTest(Base32Lowercase)(
    // Strings
    ""       -> "",
    "f"      -> "my======",
    "fo"     -> "mzxq====",
    "foo"    -> "mzxw6===",
    "foob"   -> "mzxw6yq=",
    "fooba"  -> "mzxw6ytb",
    "foobar" -> "mzxw6ytboi======",

    // Byte arrays
    Array[Byte](0x0)      -> "aa======",
    incrementingByteArray -> "aerukz4jvpg66===",
    decrementingByteArray -> "73olvgdwkqzba==="
  )

  "Base32Uppercase" in genericTest(Base32Uppercase)(
    // Strings
    ""       -> "",
    "f"      -> "MY======",
    "fo"     -> "MZXQ====",
    "foo"    -> "MZXW6===",
    "foob"   -> "MZXW6YQ=",
    "fooba"  -> "MZXW6YTB",
    "foobar" -> "MZXW6YTBOI======",

    // Byte arrays
    Array[Byte](0x0)      -> "AA======",
    incrementingByteArray -> "AERUKZ4JVPG66===",
    decrementingByteArray -> "73OLVGDWKQZBA==="
  )

  "Base64" in genericTest(Base64)(
    // Strings
    ""       -> "",
    "f"      -> "Zg==",
    "fo"     -> "Zm8=",
    "foo"    -> "Zm9v",
    "foob"   -> "Zm9vYg==",
    "fooba"  -> "Zm9vYmE=",
    "foobar" -> "Zm9vYmFy",

    // Byte arrays
    Array[Byte](0x0)      -> "AA==",
    incrementingByteArray -> "ASNFZ4mrze8=",
    decrementingByteArray -> "/ty6mHZUMhA="
  )

  "Base64URLSafe" in genericTest(Base64URLSafe)(
    // Strings
    ""       -> "",
    "f"      -> "Zg==",
    "fo"     -> "Zm8=",
    "foo"    -> "Zm9v",
    "foob"   -> "Zm9vYg==",
    "fooba"  -> "Zm9vYmE=",
    "foobar" -> "Zm9vYmFy",

    // Byte arrays
    Array[Byte](0x0)      -> "AA==",
    incrementingByteArray -> "ASNFZ4mrze8=",
    decrementingByteArray -> "_ty6mHZUMhA="
  )

  "CrockfordBase32" in genericTest(CrockfordBase32)(
    // Strings
    ""       -> "",
    "f"      -> "CR",
    "fo"     -> "CSQG",
    "foo"    -> "CSQPY",
    "foob"   -> "CSQPYRG",
    "fooba"  -> "CSQPYRK1",
    "foobar" -> "CSQPYRK1E8",

    // Byte arrays
    Array[Byte](0x0)      -> "00",
    incrementingByteArray -> "04HMASW9NF6YY",
    decrementingByteArray -> "ZVEBN63PAGS10"
  )

  "ZBase32" in genericTest(ZBase32)(
    // Strings
    ""       -> "",
    "f"      -> "ca",
    "fo"     -> "c3zo",
    "foo"    -> "c3zs6",
    "foob"   -> "c3zs6ao",
    "fooba"  -> "c3zs6aub",
    "foobar" -> "c3zs6aubqe",

    // Byte arrays
    Array[Byte](0x0)      -> "yy",
    incrementingByteArray -> "yrtwk3hjixg66",
    decrementingByteArray -> "95qmigdsko3by"
  )
end BaseNSpec
