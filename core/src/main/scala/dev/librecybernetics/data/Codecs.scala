package dev.librecybernetics.data

import dev.librecybernetics.data.alphabet.{Crockford, Multibase, RFC4648, Zooko}

object Base2 extends GenericCodec(Multibase.base2, 1, Some('='))

object Base8 extends GenericCodec(Multibase.base8, 3, Some('='))

object Base16Lowercase extends GenericCodec(RFC4648.base16Lowercase, 4, Some('='))
object Base16Uppercase extends GenericCodec(RFC4648.base16Uppercase, 4, Some('='))

object Base32HexLowercase extends GenericCodec(RFC4648.base32HexLowercase, 5, Some('='))
object Base32HexUppercase extends GenericCodec(RFC4648.base32HexUppercase, 5, Some('='))

object Base32Lowercase extends GenericCodec(RFC4648.base32Lowercase, 5, Some('='))
object Base32Uppercase extends GenericCodec(RFC4648.base32Uppercase, 5, Some('='))

object CrockfordBase32 extends GenericCodec(Crockford.base32, 5, None)
object ZBase32         extends GenericCodec(Zooko.base32, 5, None)

object Base64        extends GenericCodec(RFC4648.base64, 6, Some('='))
object Base64URLSafe extends GenericCodec(RFC4648.base64URLSafe, 6, Some('='))
