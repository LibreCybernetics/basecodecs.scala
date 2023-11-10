package dev.librecybernetics.data.codec

import dev.librecybernetics.data.alphabet.RFC4648

object Base64 extends GenericCodec(RFC4648.base64, 6, Some('='))
