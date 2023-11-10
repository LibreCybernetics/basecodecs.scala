package dev.librecybernetics.data.codec

import dev.librecybernetics.data.alphabet.RFC4648

object Base32HexUppercase extends GenericCodec(RFC4648.base32HexUppercase, 5, Some('='))
