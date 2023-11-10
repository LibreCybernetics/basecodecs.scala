package dev.librecybernetics.data.codec

import dev.librecybernetics.data.alphabet.RFC4648

object Base16Uppercase extends GenericCodec(RFC4648.base16Uppercase, 4, Some('='))
