package dev.librecybernetics.data

object Base16:
  private val DecimalAlphabet: Seq[(Byte, Char)] =
    (0 to 9).map(v => v.toByte -> (v + 48).toChar)

  val LowercaseAlphabet: Seq[(Byte, Char)] =
    DecimalAlphabet ++ (10 to 15).map(v => v.toByte -> (v + 87).toChar)

  val UppercaseAlphabet: Seq[(Byte, Char)] =
    DecimalAlphabet ++ (10 to 15).map(v => v.toByte -> (v + 55).toChar)

  def encodeUppercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .map(b => UppercaseAlphabet.find { case (v, c) => v == b }.fold(???)(_._2))
      .mkString

  def encodeUppercase(string: String): String =
    encodeUppercase(string.getBytes.toList)

  def encodeLowercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .map(b => LowercaseAlphabet.find { case (v, c) => v == b }.fold(???)(_._2))
      .mkString

  def encodeLowercase(string: String): String =
    encodeLowercase(string.getBytes.toList)

  def decodeUppercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(c => UppercaseAlphabet.find { case (v, c2) => c == c2 }.map(_._1)).toList,
      4
    )

  def decodeLowercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(c => LowercaseAlphabet.find { case (v, c2) => c == c2 }.map(_._1)).toList,
      4
    )
end Base16
