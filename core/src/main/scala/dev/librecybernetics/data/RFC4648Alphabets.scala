package dev.librecybernetics.data

object RFC4648Alphabet:
  // Decimal

  private val decimalAlphabet: PFnBijection[Byte, Char] =
    Bijection(
      {
        case i if i >= 0 && i <= 9 => (i + 48).toChar
      }: PartialFunction[Byte, Char],
      {
        case c if c >= '0' && c <= '9' => (c - 48).toByte
      }: PartialFunction[Char, Byte]
    )

  // Base16

  private val lowercaseHexAlphabet: PFnBijection[Byte, Char] =
    Bijection(
      {
        case i if i >= 10 && i <= 15 => (i + 87).toChar
      }: PartialFunction[Byte, Char],
      {
        case c if c >= 'a' && c <= 'f' => (c - 87).toByte
      }: PartialFunction[Char, Byte]
    )

  val Right(base16Lowercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ lowercaseHexAlphabet: @unchecked

  private val uppercaseHexAlphabet: PFnBijection[Byte, Char] =
    Bijection(
      {
        case i if i >= 10 && i <= 15 => (i + 55).toChar
      }: PartialFunction[Byte, Char],
      {
        case c if c >= 'A' && c <= 'F' => (c - 55).toByte
      }: PartialFunction[Char, Byte]
    )

  val Right(base16Uppercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ uppercaseHexAlphabet: @unchecked

  // Base32

  private val hexLowercaseAlphabetAlpha: PFnBijection[Byte, Char] =
    Bijection(
      {
        case i if i >= 10 && i <= 31 => (i + 87).toChar
      }: PartialFunction[Byte, Char],
      {
        case c if c >= 'a' && c <= 'v' => (c - 87).toByte
      }: PartialFunction[Char, Byte]
    )

  val Right(base32HexLowercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ hexLowercaseAlphabetAlpha: @unchecked

  private val uppercase32HexAlphabet: PFnBijection[Byte, Char] =
    Bijection(
      {
        case i if i >= 10 && i <= 31 => (i + 55).toChar
      }: PartialFunction[Byte, Char],
      {
        case c if c >= 'A' && c <= 'V' => (c - 55).toByte
      }: PartialFunction[Char, Byte]
    )

  val Right(base32HexUppercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ uppercase32HexAlphabet: @unchecked
end RFC4648Alphabet
