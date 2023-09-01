package dev.librecybernetics.data

object RFC4648Alphabets:
  // Decimal-generics

  private val decimalAlphabet: PFnBijection[Byte, Char] =
    GenericAlphabet(0, 9, '0', '9', 48)

  private val base32DecimalAlphabet: PFnBijection[Byte, Char] =
    GenericAlphabet(26, 31, '2', '7', 24)

  private val base64DecimalAlphabet: PFnBijection[Byte, Char] =
    GenericAlphabet(52, 61, '0', '9', -4)

  // Alpha-generics

  private val lowercaseStartAlphabet: PFnBijection[Byte, Char] =
    GenericAlphabet(0, 25, 'a', 'z', 97)

  private val uppercaseStartAlphabet: PFnBijection[Byte, Char] =
    GenericAlphabet(0, 25, 'A', 'Z', 65)

  private val lowercaseMiddleAlphabet: PFnBijection[Byte, Char] =
    GenericAlphabet(26, 51, 'a', 'z', 71)


  // Base16

  val Right(base16Lowercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ GenericAlphabet(10, 15, 'a', 'f', 87): @unchecked

  val Right(base16Uppercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ GenericAlphabet(10, 15, 'A', 'F', 55): @unchecked

  // Base32

  val Right(base32HexLowercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ GenericAlphabet(10, 31, 'a', 'v', 87): @unchecked

  val Right(base32HexUppercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    decimalAlphabet ++ GenericAlphabet(10, 31, 'A', 'V', 55): @unchecked

  val Right(base32Lowercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    lowercaseStartAlphabet ++ base32DecimalAlphabet: @unchecked

  val Right(base32Uppercase): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    uppercaseStartAlphabet ++ base32DecimalAlphabet: @unchecked

  // Base64

  private val base64Special: PFnBijection[Byte, Char] =
    Bijection(
      {
        case 62 => '+'
        case 63 => '/'
      }: PartialFunction[Byte, Char],
      {
        case '+' => 62
        case '/' => 63
      }: PartialFunction[Char, Byte]
    )

  val Right(base64): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    (for
      alpha           <- uppercaseStartAlphabet ++ lowercaseMiddleAlphabet
      alphanum        <- alpha ++ base64DecimalAlphabet
      alphanumspecial <- alphanum ++ base64Special
    yield alphanumspecial): @unchecked

  private val base64URLSafeSpecial: PFnBijection[Byte, Char] =
    Bijection(
      {
        case 62 => '-'
        case 63 => '_'
      }: PartialFunction[Byte, Char],
      {
        case '-' => 62
        case '_' => 63
      }: PartialFunction[Char, Byte]
    )

  val Right(base64URLSafe): Either[Bijection.Error, PFnBijection[Byte, Char]] =
    (for
      alpha           <- uppercaseStartAlphabet ++ lowercaseMiddleAlphabet
      alphanum        <- alpha ++ base64DecimalAlphabet
      alphanumspecial <- alphanum ++ base64URLSafeSpecial
    yield alphanumspecial): @unchecked
end RFC4648Alphabets
