package dev.librecybernetics.data

object Base32:
  def encodeHexUppercase(bytes: Seq[Byte]): String =
    toBase(bytes, 5)
      .flatMap(RFC4648Alphabet.base32HexUppercase.apply)
      .mkString

  def encodeHexUppercase(string: String): String =
    encodeHexUppercase(string.getBytes.toList)

  def encodeHexLowercase(bytes: Seq[Byte]): String =
    toBase(bytes, 5)
      .flatMap(RFC4648Alphabet.base32HexLowercase.apply)
      .mkString

  def encodeHexLowercase(string: String): String =
    encodeHexLowercase(string.getBytes.toList)

  def decodeHexUppercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(RFC4648Alphabet.base32HexUppercase.reverse).toList,
      5
    )

  def decodeHexLowercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(RFC4648Alphabet.base32HexLowercase.reverse).toList,
      5
    )
end Base32
