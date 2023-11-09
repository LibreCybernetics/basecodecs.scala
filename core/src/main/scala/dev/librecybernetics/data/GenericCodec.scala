package dev.librecybernetics.data

import java.nio.charset.{Charset, StandardCharsets}
import scala.util.Try

import cats.ApplicativeError
import cats.syntax.all.*

import dev.librecybernetics.data.GenericCodec.Error.UnrecognizedChar

private val defaultCharset = Charset.defaultCharset()

private[data] case class GenericCodec(
    alphabet: FnBijection[Byte, Char],
    basePower: BasePower,
    padding: Option[Char]
):
  //////////////
  // Encoding //
  //////////////

  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  def encode(bytes: Array[Byte]): String =
    val stringBuilder = StringBuilder()

    // Main content
    // NOTE: toBase(basePower) should always be encodable by the alphabet
    val encoded = toBase(bytes, basePower).map(alphabet(_))
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

  //////////////
  // Decoding //
  //////////////

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  def decode[
      F[_]: [F[_]] =>> ApplicativeError[F, GenericCodec.Error]
  ](string: String): F[Array[Byte]] =
    val merr: ApplicativeError[F, GenericCodec.Error] = implicitly

    def padLength = string.reverseIterator.takeWhile(padding.contains).length

    try
      val input = string.toCharArray
        .dropRight(padLength)
        .map(alphabet.reverse(_))
      merr.pure(fromBase(input, basePower))
    catch case e: UnrecognizedChar => merr.raiseError(e)
    end try

  inline def decode[
      F[_]: [F[_]] =>> ApplicativeError[F, GenericCodec.Error]
  ](string: String, charset: Charset): F[String] =
    decode(string).map(String(_, charset))

  inline def decodeUTF8[
      F[_]: [F[_]] =>> ApplicativeError[F, GenericCodec.Error]
  ](string: String): F[String] =
    decode(string, StandardCharsets.UTF_8)
end GenericCodec

object GenericCodec:
  enum Error extends Throwable:
    case UnrecognizedChar(char: Char)
  end Error
end GenericCodec
