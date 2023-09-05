package dev.librecybernetics.data

object MultibaseAlphabets:
  // Base2

  val base2: PFnBijection[Byte, Char] =
    GenericAlphabet(0, 1, '0', '1', 48)

  // Base8

  val base8: PFnBijection[Byte, Char] =
    GenericAlphabet(0, 7, '0', '7', 48)
end MultibaseAlphabets
