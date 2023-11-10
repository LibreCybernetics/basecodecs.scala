package dev.librecybernetics.data.codec

import dev.librecybernetics.data.alphabet.RFC4648

object Base16Lowercase extends GenericCodec(RFC4648.base16Lowercase, 4, Some('='))
