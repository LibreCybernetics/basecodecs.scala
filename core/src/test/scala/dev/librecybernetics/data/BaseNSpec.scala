package dev.librecybernetics.data

import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.prop.TableDrivenPropertyChecks.Table
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class BaseNSpec extends AnyWordSpec with ScalaCheckPropertyChecks:
  def genericTest(
      codec: GenericCodec
  )(
      examples: (Array[Byte] | String, String)*
  ): Assertion =
    forAll(Table("Input" -> "Encoded", examples*)) { (input, expected) =>
      val encoded = codec.encode(input)
      val decoded = codec.decode(expected)

      encoded shouldBe expected

      input match
        case byteArray: Array[Byte] => decoded shouldBe byteArray
        case string: String         => decoded shouldBe string.getBytes
      end match
    }

    forAll(randomByteArray(255)) { input =>
      val encoded = codec.encode(input)
      val decoded = codec.decode(encoded)

      decoded shouldBe input
    }
  end genericTest

  "Base2" in genericTest(Base2)(
    ""       -> "",
    "f"      -> "01100110",
    "fo"     -> "0110011001101111",
    "foo"    -> "011001100110111101101111",
    "foob"   -> "01100110011011110110111101100010",
    "fooba"  -> "0110011001101111011011110110001001100001",
    "foobar" -> "011001100110111101101111011000100110000101110010"
  )

  "Base8" in genericTest(Base8)(
    ""       -> "",
    "f"      -> "314",
    "fo"     -> "314674",
    "foo"    -> "31467557=",
    "foob"   -> "31467557304=",
    "fooba"  -> "31467557304604=",
    "foobar" -> "3146755730460562=="
  )

  "Base16Lowercase" in genericTest(Base16Lowercase)(
    ""       -> "",
    "f"      -> "66",
    "fo"     -> "666f",
    "foo"    -> "666f6f",
    "foob"   -> "666f6f62",
    "fooba"  -> "666f6f6261",
    "foobar" -> "666f6f626172"
  )

  "Base16Uppercase" in genericTest(Base16Uppercase)(
    ""       -> "",
    "f"      -> "66",
    "fo"     -> "666F",
    "foo"    -> "666F6F",
    "foob"   -> "666F6F62",
    "fooba"  -> "666F6F6261",
    "foobar" -> "666F6F626172"
  )

  "Base32HexLowercase" in genericTest(Base32HexLowercase)(
    ""       -> "",
    "f"      -> "co======",
    "fo"     -> "cpng====",
    "foo"    -> "cpnmu===",
    "foob"   -> "cpnmuog=",
    "fooba"  -> "cpnmuoj1",
    "foobar" -> "cpnmuoj1e8======"
  )

  "Base32HexUppercase" in genericTest(Base32HexUppercase)(
    ""       -> "",
    "f"      -> "CO======",
    "fo"     -> "CPNG====",
    "foo"    -> "CPNMU===",
    "foob"   -> "CPNMUOG=",
    "fooba"  -> "CPNMUOJ1",
    "foobar" -> "CPNMUOJ1E8======"
  )

  "Base32Lowercase" in genericTest(Base32Lowercase)(
    ""       -> "",
    "f"      -> "my======",
    "fo"     -> "mzxq====",
    "foo"    -> "mzxw6===",
    "foob"   -> "mzxw6yq=",
    "fooba"  -> "mzxw6ytb",
    "foobar" -> "mzxw6ytboi======"
  )

  "Base32Uppercase" in genericTest(Base32Uppercase)(
    ""       -> "",
    "f"      -> "MY======",
    "fo"     -> "MZXQ====",
    "foo"    -> "MZXW6===",
    "foob"   -> "MZXW6YQ=",
    "fooba"  -> "MZXW6YTB",
    "foobar" -> "MZXW6YTBOI======"
  )

  "Base64" in genericTest(Base64)(
    ""       -> "",
    "f"      -> "Zg==",
    "fo"     -> "Zm8=",
    "foo"    -> "Zm9v",
    "foob"   -> "Zm9vYg==",
    "fooba"  -> "Zm9vYmE=",
    "foobar" -> "Zm9vYmFy"
  )

  "Base64URLSafe" in genericTest(Base64URLSafe)(
    ""       -> "",
    "f"      -> "Zg==",
    "fo"     -> "Zm8=",
    "foo"    -> "Zm9v",
    "foob"   -> "Zm9vYg==",
    "fooba"  -> "Zm9vYmE=",
    "foobar" -> "Zm9vYmFy"
  )

  "ZBase32" in genericTest(ZBase32)(
    ""       -> "",
    "f"      -> "ca",
    "fo"     -> "c3zo",
    "foo"    -> "c3zs6",
    "foob"   -> "c3zs6ao",
    "fooba"  -> "c3zs6aub",
    "foobar" -> "c3zs6aubqe"
  )
end BaseNSpec
