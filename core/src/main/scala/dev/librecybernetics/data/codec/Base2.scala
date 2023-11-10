package dev.librecybernetics.data.codec

import dev.librecybernetics.data.alphabet.Multibase

object Base2 extends GenericCodec(Multibase.base2, 1, Some('='))
