package dev.librecybernetics.data

val decimalAlphabet: PFnBijection[Byte, Char] =
  Bijection(
    {
      case i if i >= 0 && i <= 9 => (i + 48).toChar
    }: PartialFunction[Byte, Char],
    {
      case c if c >= 48 && c <= 57 => (c - 48).toByte
    }: PartialFunction[Char, Byte]
  )
