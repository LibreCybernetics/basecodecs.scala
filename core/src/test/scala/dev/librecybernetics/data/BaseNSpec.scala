package dev.librecybernetics.data

import org.scalatest.Assertion
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.prop.TableDrivenPropertyChecks.Table
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class BaseNSpec extends AnyWordSpec with ScalaCheckPropertyChecks:
  def genericTest(
      encode: Array[Byte] | String => String,
      decode: String => Array[Byte]
  )(
      examples: Map[Array[Byte] | String, String]
  ): Assertion =
    forAll(Table("Input" -> "Encoded", examples.toSeq*)) { (input, expected) =>
      val encoded = encode(input)
      val decoded = decode(expected)

      encoded shouldBe expected

      input match
        case byteArray: Array[Byte] => decoded shouldBe byteArray
        case string: String         => decoded shouldBe string.getBytes
      end match
    }

    forAll(randomByteArray(255)) { input =>
      val encoded = encode(input)
      val decoded = decode(encoded)

      decoded shouldBe input
    }
  end genericTest

  "Base16Lowercase" in
    genericTest(Base16Lowercase.encode(_), Base16Lowercase.decode(_))(
      Map(
        ""       -> "",
        "f"      -> "66",
        "fo"     -> "666f",
        "foo"    -> "666f6f",
        "foob"   -> "666f6f62",
        "fooba"  -> "666f6f6261",
        "foobar" -> "666f6f626172"
      )
    )

  "Base16Uppercase" in
    genericTest(Base16Uppercase.encode(_), Base16Uppercase.decode(_))(
      Map(
        ""       -> "",
        "f"      -> "66",
        "fo"     -> "666F",
        "foo"    -> "666F6F",
        "foob"   -> "666F6F62",
        "fooba"  -> "666F6F6261",
        "foobar" -> "666F6F626172"
      )
    )

  "Base32HexLowercase" in
    genericTest(Base32HexLowercase.encode(_), Base32HexLowercase.decode(_))(
      Map(
        ""       -> "",
        "f"      -> "co",
        "fo"     -> "cpng",
        "foo"    -> "cpnmu",
        "foob"   -> "cpnmuog",
        "fooba"  -> "cpnmuoj1",
        "foobar" -> "cpnmuoj1e8"
      )
    )

  "Base32HexUppercase" in
    genericTest(Base32HexUppercase.encode(_), Base32HexUppercase.decode(_))(
      Map(
        ""       -> "",
        "f"      -> "CO",
        "fo"     -> "CPNG",
        "foo"    -> "CPNMU",
        "foob"   -> "CPNMUOG",
        "fooba"  -> "CPNMUOJ1",
        "foobar" -> "CPNMUOJ1E8"
      )
    )

  "Base32Lowercase" in
    genericTest(Base32Lowercase.encode(_), Base32Lowercase.decode(_))(
      Map(
        ""       -> "",
        "f"      -> "my",
        "fo"     -> "mzxq",
        "foo"    -> "mzxw6",
        "foob"   -> "mzxw6yq",
        "fooba"  -> "mzxw6ytb",
        "foobar" -> "mzxw6ytboi"
      )
    )

  "Base32Uppercase" in
    genericTest(Base32Uppercase.encode(_), Base32Uppercase.decode(_))(
      Map(
        ""       -> "",
        "f"      -> "MY",
        "fo"     -> "MZXQ",
        "foo"    -> "MZXW6",
        "foob"   -> "MZXW6YQ",
        "fooba"  -> "MZXW6YTB",
        "foobar" -> "MZXW6YTBOI"
      )
    )

  "Base64" in
    genericTest(Base64.encode(_), Base64.decode(_))(
      Map(
        ""       -> "",
        "f"      -> "Zg",
        "fo"     -> "Zm8",
        "foo"    -> "Zm9v",
        "foob"   -> "Zm9vYg",
        "fooba"  -> "Zm9vYmE",
        "foobar" -> "Zm9vYmFy"
      )
    )

  "Base64URLSafe" in
    genericTest(Base64URLSafe.encode(_), Base64URLSafe.decode(_))(
      Map(
        ""       -> "",
        "f"      -> "Zg",
        "fo"     -> "Zm8",
        "foo"    -> "Zm9v",
        "foob"   -> "Zm9vYg",
        "fooba"  -> "Zm9vYmE",
        "foobar" -> "Zm9vYmFy"
      )
    )
end BaseNSpec
