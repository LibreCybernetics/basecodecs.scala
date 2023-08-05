package dev.librecybernetics.data

object Base32:
  val LowercaseAlphabet: MapBijection[Byte, Char] =
    MapBijection((0 to 25).map(v => v.toByte -> (v + 97).toChar)*) ++
      MapBijection((26 to 31).map(v => v.toByte -> (v + 22).toChar)*)

  val UppercaseAlphabet: MapBijection[Byte, Char] =
    MapBijection((0 to 25).map(v => v.toByte -> (v + 65).toChar)*) ++
      MapBijection((26 to 31).map(v => v.toByte -> (v + 22).toChar)*)

  val HexLowercaseAlphabet: MapBijection[Byte, Char] =
    DecimalAlphabet ++ MapBijection((10 to 31).map(v => v.toByte -> (v + 87).toChar)*)

  val HexUppercaseAlphabet: MapBijection[Byte, Char] =
    DecimalAlphabet ++ (10 to 31).map(v => v.toByte -> (v + 55).toChar)

  def encodeHexUppercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(HexUppercaseAlphabet.apply)
      .mkString

  def encodeHexUppercase(string: String): String =
    encodeHexUppercase(string.getBytes.toList)

  def encodeHexLowercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(HexLowercaseAlphabet.apply)
      .mkString

  def encodeHexLowercase(string: String): String =
    encodeHexLowercase(string.getBytes.toList)

  def decodeHexUppercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(HexUppercaseAlphabet.reverse),
      4
    )

  def decodeHexLowercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(HexLowercaseAlphabet.reverse),
      4
    )
end Base32
