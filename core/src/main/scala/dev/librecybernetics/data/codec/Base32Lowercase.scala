package dev.librecybernetics.data.codec

import dev.librecybernetics.data.alphabet.RFC4648

object Base32Lowercase extends GenericCodec(RFC4648.base32Lowercase, 5, Some('='))
