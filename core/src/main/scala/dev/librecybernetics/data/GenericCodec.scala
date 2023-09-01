package dev.librecybernetics.data

import java.nio.charset.{Charset, StandardCharsets}

private val defaultCharset = Charset.defaultCharset()

case class GenericCodec(
    alphabet: PFnBijection[Byte, Char],
    basePower: BasePower
):
  def encode(bytes: Array[Byte]): String =
    toBase(bytes.toList, basePower)
      .flatMap(alphabet.apply)
      .mkString

  def encode(string: String, charset: Charset): String =
    encode(string.getBytes(charset))

  def encode(string: String): String =
    encode(string, defaultCharset)

  def decode(string: String): Array[Byte] =
    fromBase(string.flatMap(alphabet.reverse).toList, basePower).toArray

  def decode(string: String, charset: Charset): String =
    String(decode(string), charset)

  def decodeUTF8(string: String): String =
    decode(string, StandardCharsets.UTF_8)
end GenericCodec
