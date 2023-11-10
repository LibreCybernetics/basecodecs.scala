package dev.librecybernetics.data.codec

import dev.librecybernetics.data.alphabet.Crockford

/** Crockford's Base32 encoding.
  *
  * NOTICE: Decoding is case-insensitive and includes almost-homoglyphs / visually-similar characters.
  *
  * Reference: [[https://www.crockford.com/base32.html]]
  */
object CrockfordBase32 extends GenericCodec(Crockford.base32, 5, None)
