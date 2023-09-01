package dev.librecybernetics.data

import scala.annotation.tailrec
import scala.collection.immutable.ArraySeq
import scala.util.NotGiven

private[librecybernetics] type BasePower = 4 | 5 | 6

private[librecybernetics] def byte2Short(byte: Byte) =
  if (byte < 0) (byte + 256).toShort else byte.toShort

private[librecybernetics] def mask(basePower: Int): Byte = ((1 << basePower) - 1).toByte

private[librecybernetics] def toBasePartialByte(
    currentByte: Byte,
    nextByte: Byte,
    remainingBits: Int,
    basePower: BasePower
): Byte =
  val currentBits: Byte =
    ((currentByte & mask(remainingBits)) << (basePower - remainingBits)).toByte
  val nextBits: Byte    =
    (byte2Short(nextByte) >> (8 - basePower + remainingBits)).toByte

  (currentBits | nextBits).toByte
end toBasePartialByte

@tailrec
private[librecybernetics] def toBasePartial[S <: Seq[Byte]](
    input: S,
    remainingBits: Int,
    basePower: BasePower,
    result: S
)(using
    NotGiven[S <:< ArraySeq[Byte]],
    NotGiven[S <:< Vector[Byte]]
): Seq[Byte] =
  input match
    case Nil =>
      result

    case input @ x :: xs if remainingBits > basePower =>
      val bits = byte2Short(x) >> (remainingBits - basePower)
      toBasePartial(input, remainingBits - basePower, basePower, result :+ (bits & mask(basePower)).toByte)

    case x :: xs if remainingBits == basePower =>
      toBasePartial(xs, 8, basePower, result :+ (x & mask(basePower)).toByte)

    case x :: xs if remainingBits < basePower =>
      val bits = toBasePartialByte(x, xs.headOption.getOrElse(0), remainingBits, basePower)
      toBasePartial(xs, 8 - (basePower - remainingBits), basePower, result :+ bits)
  end match

def toBase[S <: Seq[Byte]](
    input: S,
    basePower: BasePower
)(using
    NotGiven[S <:< ArraySeq[Byte]],
    NotGiven[S <:< Vector[Byte]]
): Seq[Byte] =
  toBasePartial(input, 8, basePower, Nil)

@tailrec
private[librecybernetics] def fromBasePartial[S <: Seq[Byte]](
    input: S,
    missingBits: Int,
    basePower: BasePower,
    result: S
)(using
    NotGiven[S <:< ArraySeq[Byte]],
    NotGiven[S <:< Vector[Byte]]
): Seq[Byte] =
  val r: Byte = result.lastOption.getOrElse(0)

  input match
    case Nil => result

    case input @ _ :: _ if missingBits == 0 =>
      fromBasePartial(input, 8, basePower, result :+ 0.toByte)

    case x :: xs if missingBits >= basePower =>
      val bits     = (x << (missingBits - basePower)).toByte
      val mutatedR = (r | bits).toByte
      fromBasePartial(xs, missingBits - basePower, basePower, result.dropRight(1) :+ mutatedR)

    case x :: Nil if missingBits < basePower =>
      val bitsC    = (byte2Short(x) >> (basePower - missingBits)).toByte
      val mutatedR = (r | bitsC).toByte
      result.dropRight(1) :+ mutatedR

    case x :: xs if missingBits < basePower =>
      val bitsC    = (byte2Short(x) >> (basePower - missingBits)).toByte
      val bitsN    = ((x & mask(basePower - missingBits)) << (8 - (basePower - missingBits))).toByte
      val mutatedR = (r | bitsC).toByte
      fromBasePartial(xs, 8 - (basePower - missingBits), basePower, result.dropRight(1) :+ mutatedR :+ bitsN)
  end match
end fromBasePartial

def fromBase[S <: Seq[Byte]](
    input: S,
    basePower: BasePower
)(using
    NotGiven[S <:< ArraySeq[Byte]],
    NotGiven[S <:< Vector[Byte]]
): Seq[Byte] =
  fromBasePartial(input, 0, basePower, Nil)
