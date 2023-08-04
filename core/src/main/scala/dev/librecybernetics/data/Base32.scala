package dev.librecybernetics.data

object Base32:
  val LowercaseAlphabet: Bijection[Byte, Char] =
    Bijection((0 to 25).map(v => v.toByte -> (v + 97).toChar)*) ++
      Bijection((26 to 31).map(v => v.toByte -> (v + 22).toChar)*)

  val UppercaseAlphabet: Bijection[Byte, Char] =
    Bijection((0 to 25).map(v => v.toByte -> (v + 65).toChar)*) ++
      Bijection((26 to 31).map(v => v.toByte -> (v + 22).toChar)*)

  val HexLowercaseAlphabet: Bijection[Byte, Char] =
    DecimalAlphabet ++ Bijection((10 to 31).map(v => v.toByte -> (v + 87).toChar)*)

  val HexUppercaseAlphabet: Bijection[Byte, Char] =
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
