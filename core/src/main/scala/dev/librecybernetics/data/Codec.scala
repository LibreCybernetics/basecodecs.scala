package dev.librecybernetics.data

import java.nio.charset.Charset

import cats.ApplicativeError
import cats.syntax.all.*

trait Codec:
  def encode(bytes: Array[Byte]): String

  def decode[
    F[_] : [F[_]] =>> ApplicativeError[F, CodecError]
  ](string: String): F[Array[Byte]]
end Codec

object Codec:
  private val defaultCharset = Charset.defaultCharset()

  extension (codec: Codec)
    inline def encode(string: String, charset: Charset): String =
      codec.encode(string.getBytes(charset))

    inline def encode(string: String): String =
      codec.encode(string, defaultCharset)

    inline def encode(input: Array[Byte] | String): String =
      input match
        case bytes: Array[Byte] => codec.encode(bytes)
        case string: String => codec.encode(string)
      end match
    end encode

    inline def decodeString[
      F[_] : [F[_]] =>> ApplicativeError[F, CodecError]
    ](string: String, charset: Charset): F[String] =
      codec.decode(string).map(String(_, charset))

    inline def decodeString[
      F[_] : [F[_]] =>> ApplicativeError[F, CodecError]
    ](string: String): F[String] =
      codec.decodeString(string, defaultCharset)
  end extension
end Codec