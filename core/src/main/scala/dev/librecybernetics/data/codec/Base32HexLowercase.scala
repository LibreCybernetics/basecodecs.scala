package dev.librecybernetics.data.codec

import dev.librecybernetics.data.alphabet.RFC4648

object Base32HexLowercase extends GenericCodec(RFC4648.base32HexLowercase, 5, Some('='))
