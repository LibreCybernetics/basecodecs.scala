package dev.librecybernetics.data

object Base16:
  private val lowercaseAlphabetAlpha: PFnBijection[Byte, Char] =
    Bijection(
      {
        case i if i >= 10 && i <= 15 => (i + 87).toChar
      }: PartialFunction[Byte, Char],
      {
        case c if c >= 'a' && c <= 'f' => (c - 87).toByte
      }: PartialFunction[Char, Byte]
    )

  val Right(lowercaseAlphabet): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ lowercaseAlphabetAlpha: @unchecked

  private val uppercaseAlphabetAlpha: PFnBijection[Byte, Char] =
    Bijection(
      {
        case i if i >= 10 && i <= 15 => (i + 55).toChar
      }: PartialFunction[Byte, Char],
      {
        case c if c >= 'A' && c <= 'F' => (c - 55).toByte
      }: PartialFunction[Char, Byte]
    )

  val Right(uppercaseAlphabet): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ uppercaseAlphabetAlpha: @unchecked

  def encodeUppercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(uppercaseAlphabet.apply)
      .mkString

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
