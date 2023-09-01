package dev.librecybernetics.data

object Base16:
  def encodeUppercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(RFC4648Alphabet.base16Uppercase.apply)
      .mkString

  def encodeUppercase(string: String): String =
    encodeUppercase(string.getBytes.toList)

  def encodeLowercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(RFC4648Alphabet.base16Lowercase.apply)
      .mkString

  def encodeLowercase(string: String): String =
    encodeLowercase(string.getBytes.toList)

  def decodeUppercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(RFC4648Alphabet.base16Uppercase.reverse).toList,
      4
    )

  def decodeLowercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(RFC4648Alphabet.base16Lowercase.reverse).toList,
      4
    )
end Base16
