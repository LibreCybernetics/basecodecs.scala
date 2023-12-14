package dev.librecybernetics.data.codec

import java.nio.charset.{Charset, StandardCharsets}
import scala.util.Try

import cats.ApplicativeError
import cats.syntax.all.*

import dev.librecybernetics.data.CodecError.UnrecognizedChar
import dev.librecybernetics.data.{Codec, CodecError, FnBijection}
import dev.librecybernetics.data.util.*

private[data] case class GenericCodec(
    alphabet: FnBijection[Byte, Char],
    basePower: BasePower,
    padding: Option[Char]
) extends Codec:
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

  //////////////
  // Decoding //
  //////////////

  def decode[
      F[_]: [F[_]] =>> ApplicativeError[F, CodecError]
  ](string: String): F[Array[Byte]] =
    val merr: ApplicativeError[F, CodecError] = implicitly

    def padLength = string.reverseIterator.takeWhile(padding.contains).length

    try
      val input = string.toCharArray
        .dropRight(padLength)
        .map(alphabet.reverse(_))
      merr.pure(fromBase(input, basePower))
    catch case e: UnrecognizedChar => merr.raiseError(e)
    end try
end GenericCodec
