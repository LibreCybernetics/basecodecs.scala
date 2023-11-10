package dev.librecybernetics.data.codec

import dev.librecybernetics.data.alphabet.Multibase

object Base8 extends GenericCodec(Multibase.base8, 3, Some('='))
