package dev.librecybernetics.data

object Base2 extends GenericCodec(MultibaseAlphabets.base2, 1, Some('='))

object Base8 extends GenericCodec(MultibaseAlphabets.base8, 3, Some('='))

object Base16Lowercase extends GenericCodec(RFC4648Alphabets.base16Lowercase, 4, Some('='))
object Base16Uppercase extends GenericCodec(RFC4648Alphabets.base16Uppercase, 4, Some('='))

object Base32HexLowercase extends GenericCodec(RFC4648Alphabets.base32HexLowercase, 5, Some('='))
object Base32HexUppercase extends GenericCodec(RFC4648Alphabets.base32HexUppercase, 5, Some('='))

object Base32Lowercase extends GenericCodec(RFC4648Alphabets.base32Lowercase, 5, Some('='))
object Base32Uppercase extends GenericCodec(RFC4648Alphabets.base32Uppercase, 5, Some('='))

object CrockfordBase32 extends GenericCodec(CrockfordAlphabet.crockfordBase32, 5, None)
object ZBase32         extends GenericCodec(ZookoAlphabet.zBase32, 5, None)

object Base64        extends GenericCodec(RFC4648Alphabets.base64, 6, Some('='))
object Base64URLSafe extends GenericCodec(RFC4648Alphabets.base64URLSafe, 6, Some('='))
