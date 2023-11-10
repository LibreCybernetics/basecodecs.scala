package dev.librecybernetics.data.codec

import dev.librecybernetics.data.alphabet.RFC4648

object Base32Uppercase extends GenericCodec(RFC4648.base32Uppercase, 5, Some('='))
