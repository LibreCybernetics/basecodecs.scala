package dev.librecybernetics.data

val Right(decimalAlphabet): Either[Bijection.Error, MapBijection[Byte, Char]] =
  MapBijection((0 to 9).map(v => v.toByte -> (v + 48).toChar)*): @unchecked
