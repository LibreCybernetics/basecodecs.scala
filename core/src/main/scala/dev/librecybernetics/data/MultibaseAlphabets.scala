package dev.librecybernetics.data

object MultibaseAlphabets:
  // Base2

  val base2: FnBijection[Byte, Char] =
    Bijection(
      { case i if 0 <= i && i <= 1 => (i + 48).toChar },
      { case c if '0' <= c && c <= '1' => (c - 48).toByte }
    )

  // Base8

  val base8: FnBijection[Byte, Char] =
    Bijection(
      { case i if 0 <= i && i <= 7 => (i + 48).toChar },
      { case c if '0' <= c && c <= '7' => (c - 48).toByte }
    )
end MultibaseAlphabets
