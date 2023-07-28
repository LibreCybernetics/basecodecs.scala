package dev.librecybernetics.data

object Base16:
  private val DecimalAlphabet: Map[Byte, Char] =
    (0 to 9).map(v => v.toByte -> (v + 48).toChar).toMap

  val LowercaseAlphabet: Map[Byte, Char] =
    DecimalAlphabet ++ (10 to 15).map(v => v.toByte -> (v + 87).toChar).toMap

  val UppercaseAlphabet: Map[Byte, Char] =
    DecimalAlphabet ++ (10 to 15).map(v => v.toByte -> (v + 55).toChar).toMap

  def encodeUppercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4).map(UppercaseAlphabet(_)).mkString

  def encodeUppercase(string: String): String =
    encodeUppercase(string.getBytes.toList)

  def encodeLowercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4).map(LowercaseAlphabet(_)).mkString

  def encodeLowercase(string: String): String =
    encodeLowercase(string.getBytes.toList)
end Base16
