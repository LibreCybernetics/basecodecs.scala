package dev.librecybernetics.data

object Base16Lowercase extends GenericCodec(RFC4648Alphabets.base16Lowercase, 4, '=')
object Base16Uppercase extends GenericCodec(RFC4648Alphabets.base16Uppercase, 4, '=')

object Base32HexLowercase extends GenericCodec(RFC4648Alphabets.base32HexLowercase, 5, '=')
object Base32HexUppercase extends GenericCodec(RFC4648Alphabets.base32HexUppercase, 5, '=')

object Base32Lowercase extends GenericCodec(RFC4648Alphabets.base32Lowercase, 5, '=')
object Base32Uppercase extends GenericCodec(RFC4648Alphabets.base32Uppercase, 5, '=')

object Base64        extends GenericCodec(RFC4648Alphabets.base64, 6, '=')
object Base64URLSafe extends GenericCodec(RFC4648Alphabets.base64URLSafe, 6, '=')
