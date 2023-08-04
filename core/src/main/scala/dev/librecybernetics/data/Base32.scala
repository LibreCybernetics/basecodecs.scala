package dev.librecybernetics.data

object Base32:
  val LowercaseAlphabet: Seq[(Byte, Char)] =
    (0 to 25).map(v => v.toByte -> (v + 97).toChar) ++
      (26 to 31).map(v => v.toByte -> (v + 22).toChar)

  val UppercaseAlphabet: Seq[(Byte, Char)] =
    (0 to 25).map(v => v.toByte -> (v + 65).toChar) ++
      (26 to 31).map(v => v.toByte -> (v + 22).toChar)

  val HexLowercaseAlphabet: Seq[(Byte, Char)] =
    DecimalAlphabet ++ (10 to 31).map(v => v.toByte -> (v + 87).toChar)

  val HexUppercaseAlphabet: Seq[(Byte, Char)] =
    DecimalAlphabet ++ (10 to 31).map(v => v.toByte -> (v + 55).toChar)

  def encodeHexUppercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(b => HexUppercaseAlphabet.find { case (v, c) => v == b }.map(_._2))
      .mkString

  def encodeHexUppercase(string: String): String =
    encodeHexUppercase(string.getBytes.toList)

  def encodeHexLowercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(b => HexLowercaseAlphabet.find { case (v, c) => v == b }.map(_._2))
      .mkString

  def encodeHexLowercase(string: String): String =
    encodeHexLowercase(string.getBytes.toList)

  def decodeHexUppercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(c => HexUppercaseAlphabet.find { case (v, c2) => c == c2 }.map(_._1)).toList,
      4
    )

  def decodeHexLowercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(c => HexLowercaseAlphabet.find { case (v, c2) => c == c2 }.map(_._1)).toList,
      4
    )
end Base32
