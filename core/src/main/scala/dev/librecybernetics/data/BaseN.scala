package dev.librecybernetics.data

object Base16Lowercase extends GenericCodec(RFC4648Alphabets.base16Lowercase, 4)
object Base16Uppercase extends GenericCodec(RFC4648Alphabets.base16Uppercase, 4)

object Base32HexLowercase extends GenericCodec(RFC4648Alphabets.base32HexLowercase, 5)
object Base32HexUppercase extends GenericCodec(RFC4648Alphabets.base32HexUppercase, 5)
