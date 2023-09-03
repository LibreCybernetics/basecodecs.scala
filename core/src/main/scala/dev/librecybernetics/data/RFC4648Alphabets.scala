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

  val base16Lowercase: PFnBijection[Byte, Char] =
    decimalAlphabet ++ GenericAlphabet(10, 15, 'a', 'f', 87)

  val base16Uppercase: PFnBijection[Byte, Char] =
    decimalAlphabet ++ GenericAlphabet(10, 15, 'A', 'F', 55)

  // Base32

  val base32HexLowercase: PFnBijection[Byte, Char] =
    decimalAlphabet ++ GenericAlphabet(10, 31, 'a', 'v', 87)

  val base32HexUppercase: PFnBijection[Byte, Char] =
    decimalAlphabet ++ GenericAlphabet(10, 31, 'A', 'V', 55)

  val base32Lowercase: PFnBijection[Byte, Char] =
    lowercaseStartAlphabet ++ base32DecimalAlphabet

  val base32Uppercase: PFnBijection[Byte, Char] =
    uppercaseStartAlphabet ++ base32DecimalAlphabet

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

  val base64: PFnBijection[Byte, Char] =
    uppercaseStartAlphabet ++ lowercaseMiddleAlphabet ++ base64DecimalAlphabet ++ base64Special

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

  val base64URLSafe: PFnBijection[Byte, Char] =
    uppercaseStartAlphabet ++ lowercaseMiddleAlphabet ++ base64DecimalAlphabet ++ base64URLSafeSpecial
end RFC4648Alphabets
