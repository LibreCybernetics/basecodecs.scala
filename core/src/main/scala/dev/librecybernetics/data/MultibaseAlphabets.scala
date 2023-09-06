package dev.librecybernetics.data

object MultibaseAlphabets:
  // Base2

  val base2: PFnBijection[Byte, Char] =
    Bijection(
      {
        case i if 0 <= i && i <= 1 => (i + 48).toChar
      }: PartialFunction[Byte, Char],
      {
        case c if '0' <= c && c <= '1' => (c - 48).toByte
      }: PartialFunction[Char, Byte]
    )

  // Base8

  val base8: PFnBijection[Byte, Char] =
    Bijection(
      {
        case i if 0 <= i && i <= 7 => (i + 48).toChar
      }: PartialFunction[Byte, Char],
      {
        case c if '0' <= c && c <= '7' => (c - 48).toByte
      }: PartialFunction[Char, Byte]
    )
end MultibaseAlphabets
