package dev.librecybernetics.data.codec

import dev.librecybernetics.data.alphabet.RFC4648
object Base64URLSafe extends GenericCodec(RFC4648.base64URLSafe, 6, Some('='))
