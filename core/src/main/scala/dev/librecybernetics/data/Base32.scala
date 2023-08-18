package dev.librecybernetics.data

object Base32:
  private val hexLowercaseAlphabetAlpha: PFnBijection[Byte, Char] =
    Bijection(
      {
        case i if i >= 10 && i <= 31 => (i + 87).toChar
      }: PartialFunction[Byte, Char],
      {
        case c if c >= 'a' && c <= 'v' => (c - 87).toByte
      }: PartialFunction[Char, Byte]
    )

  val Right(hexLowercaseAlphabet): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ hexLowercaseAlphabetAlpha: @unchecked

  private val hexUppercaseAlphabetAlpha: PFnBijection[Byte, Char] =
    Bijection(
      {
        case i if i >= 10 && i <= 31 => (i + 55).toChar
      }: PartialFunction[Byte, Char],
      {
        case c if c >= 'A' && c <= 'V' => (c - 55).toByte
      }: PartialFunction[Char, Byte]
    )

  val Right(hexUppercaseAlphabet): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ hexUppercaseAlphabetAlpha: @unchecked

  def encodeHexUppercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(hexUppercaseAlphabet.apply)
      .mkString

  def encodeHexUppercase(string: String): String =
    encodeHexUppercase(string.getBytes.toList)

  def encodeHexLowercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(hexLowercaseAlphabet.apply)
      .mkString

  def encodeHexLowercase(string: String): String =
    encodeHexLowercase(string.getBytes.toList)

  def decodeHexUppercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(hexUppercaseAlphabet.reverse),
      4
    )

  def decodeHexLowercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(hexLowercaseAlphabet.reverse),
      4
    )
end Base32
