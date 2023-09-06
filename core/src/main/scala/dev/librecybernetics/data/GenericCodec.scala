package dev.librecybernetics.data

import java.nio.charset.{Charset, StandardCharsets}

private val defaultCharset = Charset.defaultCharset()

case class GenericCodec(
    alphabet: PFnBijection[Byte, Char],
    basePower: BasePower,
    padding: Char
):
  // toBase(basePower) should always be encodable by the alphabet
  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  def encode(bytes: Array[Byte]): String =
    val stringBuilder = StringBuilder()
    stringBuilder.appendAll(toBase(bytes, basePower).map(alphabet(_).get))
    stringBuilder.toString()

  inline def encode(string: String, charset: Charset): String =
    encode(string.getBytes(charset))

  inline def encode(string: String): String =
    encode(string, defaultCharset)

  inline def encode(input: Array[Byte] | String): String =
    input match
      case bytes: Array[Byte] => encode(bytes)
      case string: String     => encode(string)
    end match
  end encode

  def decode(string: String): Array[Byte] =
    fromBase(
      string.takeWhile(_ != padding).flatMap(alphabet.reverse(_)).toArray,
      basePower
    )

  inline def decode(string: String, charset: Charset): String =
    String(decode(string), charset)

  inline def decodeUTF8(string: String): String =
    decode(string, StandardCharsets.UTF_8)
end GenericCodec

object GenericCodec:
  inline def apply(
      alphabet: PFnBijection[Byte, Char],
      basePower: BasePower
  ): GenericCodec = GenericCodec(alphabet, basePower, '=')
end GenericCodec
