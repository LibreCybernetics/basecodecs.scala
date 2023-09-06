package dev.librecybernetics.data

import java.nio.charset.{Charset, StandardCharsets}

private val defaultCharset = Charset.defaultCharset()

case class GenericCodec(
    alphabet: PFnBijection[Byte, Char],
    basePower: BasePower,
    padding: Option[Char]
):
  // toBase(basePower) should always be encodable by the alphabet
  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  def encode(bytes: Array[Byte]): String =
    val stringBuilder = StringBuilder()

    // Main content
    val encoded = toBase(bytes, basePower).map(alphabet(_).get)
    stringBuilder.appendAll(encoded)

    // Padding
    val fillSize = {
      val bs = blockSize(basePower)
      if (encoded.length % bs == 0) 0 else bs - (encoded.length % bs)
    }
    val pad      = padding match
      case Some(c) => Array.fill(fillSize)(c)
      case None    => Array.emptyCharArray
    stringBuilder.appendAll(pad)

    stringBuilder.toString()
  end encode

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
      string.takeWhile(c => !padding.contains(c)).flatMap(alphabet.reverse(_)).toArray,
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
  ): GenericCodec = GenericCodec(alphabet, basePower, Some('='))
end GenericCodec
