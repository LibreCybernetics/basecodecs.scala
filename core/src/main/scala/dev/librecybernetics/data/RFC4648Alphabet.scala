package dev.librecybernetics.data

object RFC4648Alphabet:
  // Decimal

  private val decimalAlphabet: PFnBijection[Byte, Char] =
    GenericAlphabet(0, 9, '0', '9', 48)

  // Base16

  val Right(base16Lowercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ GenericAlphabet(10, 15, 'a', 'f', 87) : @unchecked

  val Right(base16Uppercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ GenericAlphabet(10, 15, 'A', 'F', 55): @unchecked

  // Base32

  val Right(base32HexLowercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ GenericAlphabet(10, 31, 'a', 'v', 87): @unchecked

  val Right(base32HexUppercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ GenericAlphabet(10, 31, 'A', 'V', 55): @unchecked
end RFC4648Alphabet
