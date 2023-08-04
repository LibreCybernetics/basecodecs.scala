package dev.librecybernetics.data

object Base16:
  val LowercaseAlphabet: Bijection[Byte, Char] =
    DecimalAlphabet ++ Bijection((10 to 15).map(v => v.toByte -> (v + 87).toChar)*)

  val UppercaseAlphabet: Bijection[Byte, Char] =
    DecimalAlphabet ++ Bijection((10 to 15).map(v => v.toByte -> (v + 55).toChar)*)

  def encodeUppercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(UppercaseAlphabet.apply)
      .mkString

  println(UppercaseAlphabet)
  def encodeUppercase(string: String): String =
    encodeUppercase(string.getBytes.toList)

  def encodeLowercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(LowercaseAlphabet.apply)
      .mkString

  def encodeLowercase(string: String): String =
    encodeLowercase(string.getBytes.toList)

  def decodeUppercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(UppercaseAlphabet.reverse).toList,
      4
    )

  def decodeLowercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(LowercaseAlphabet.reverse).toList,
      4
    )
end Base16
