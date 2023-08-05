package dev.librecybernetics.data

object Base16:
  private val Right(lowercaseAlphabetAlpha): Either[Bijection.Error, MapBijection[Byte, Char]] =
    MapBijection((10 to 15).map(v => v.toByte -> (v + 87).toChar)*): @unchecked

  val Right(lowercaseAlphabet): Either[Bijection.Error, MapBijection[Byte, Char]] =
    decimalAlphabet ++ lowercaseAlphabetAlpha: @unchecked

  private val Right(uppercaseAlphabetAlpha): Either[Bijection.Error, MapBijection[Byte, Char]] =
    MapBijection((10 to 15).map(v => v.toByte -> (v + 55).toChar)*): @unchecked

  val Right(uppercaseAlphabet): Either[Bijection.Error, MapBijection[Byte, Char]] =
    decimalAlphabet ++ uppercaseAlphabetAlpha: @unchecked

  def encodeUppercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(uppercaseAlphabet.apply)
      .mkString

  println(uppercaseAlphabet)
  def encodeUppercase(string: String): String =
    encodeUppercase(string.getBytes.toList)

  def encodeLowercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(lowercaseAlphabet.apply)
      .mkString

  def encodeLowercase(string: String): String =
    encodeLowercase(string.getBytes.toList)

  def decodeUppercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(uppercaseAlphabet.reverse).toList,
      4
    )

  def decodeLowercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(lowercaseAlphabet.reverse).toList,
      4
    )
end Base16
