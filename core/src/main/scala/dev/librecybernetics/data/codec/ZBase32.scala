package dev.librecybernetics.data.codec

import dev.librecybernetics.data.alphabet.Zooko

/** Zooko's Base32 or zBase32
  *
  * Reference: [[https://philzimmermann.com/docs/human-oriented-base-32-encoding.txt]]
  */
object ZBase32 extends GenericCodec(Zooko.base32, 5, None)



