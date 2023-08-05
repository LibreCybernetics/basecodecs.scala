package dev.librecybernetics.data

object Base32:
//  val LowercaseAlphabet: MapBijection[Byte, Char] =
//    MapBijection((0 to 25).map(v => v.toByte -> (v + 97).toChar)*) ++
//      MapBijection((26 to 31).map(v => v.toByte -> (v + 22).toChar)*)
//
//  val UppercaseAlphabet: MapBijection[Byte, Char] =
//    MapBijection((0 to 25).map(v => v.toByte -> (v + 65).toChar)*) ++
//      MapBijection((26 to 31).map(v => v.toByte -> (v + 22).toChar)*)

  private val Right(hexLowercaseAlphabetAlpha): Either[Bijection.Error, MapBijection[Byte, Char]] =
    MapBijection((10 to 31).map(v => v.toByte -> (v + 87).toChar)*): @unchecked

  val Right(hexLowercaseAlphabet): Either[Bijection.Error, MapBijection[Byte, Char]] =
    decimalAlphabet ++ hexLowercaseAlphabetAlpha: @unchecked

  private val Right(hexUppercaseAlphabetAlpha): Either[Bijection.Error, MapBijection[Byte, Char]] =
    MapBijection((10 to 31).map(v => v.toByte -> (v + 55).toChar)*): @unchecked

  val Right(hexUppercaseAlphabet): Either[Bijection.Error, MapBijection[Byte, Char]] =
    decimalAlphabet ++ hexUppercaseAlphabetAlpha: @unchecked


  def encodeHexUppercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(hexUppercaseAlphabet.apply)
      .mkString

  def encodeHexUppercase(string: String): String =
    encodeHexUppercase(string.getBytes.toList)

  def encodeHexLowercase(bytes: Seq[Byte]): String =
    toBase(bytes, 4)
      .flatMap(hexLowercaseAlphabet.apply)
      .mkString

  def encodeHexLowercase(string: String): String =
    encodeHexLowercase(string.getBytes.toList)

  def decodeHexUppercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(hexUppercaseAlphabet.reverse),
      4
    )

  def decodeHexLowercase(string: String): Seq[Byte] =
    fromBase(
      string.flatMap(hexLowercaseAlphabet.reverse),
      4
    )
end Base32
