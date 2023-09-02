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
private[librecybernetics] def toBasePartial(
    input: Array[Byte],
    remainingBits: Int,
    basePower: BasePower,
    result: Array[Byte]
): Array[Byte] =
  input.headOption match
    case None => result

    case Some(x) =>
      val xs = input.tail

      remainingBits compare basePower match
        case 1 => // GT
          val bits = byte2Short(x) >> (remainingBits - basePower)
          toBasePartial(input, remainingBits - basePower, basePower, result :+ (bits & mask(basePower)).toByte)

        case 0 => // EQ
          toBasePartial(xs, 8, basePower, result :+ (x & mask(basePower)).toByte)

        case -1 => // LT
          val bits = toBasePartialByte(x, xs.headOption.getOrElse(0), remainingBits, basePower)
          toBasePartial(xs, 8 - (basePower - remainingBits), basePower, result :+ bits)
  end match
end toBasePartial

def toBase[S <: Array[Byte]](
    input: Array[Byte],
    basePower: BasePower
): Array[Byte] =
  toBasePartial(input, 8, basePower, Array.emptyByteArray)

@tailrec
private[librecybernetics] def fromBasePartial(
    input: Array[Byte],
    missingBits: Int,
    basePower: BasePower,
    result: Array[Byte]
): Array[Byte] =
  val r: Byte = result.lastOption.getOrElse(0)

  input.headOption match
    case None => result

    case Some(x) =>
      val xs = input.tail

      if (missingBits == 0) fromBasePartial(input, 8, basePower, result :+ 0.toByte)
      else
        missingBits compare basePower match
          case 0 | 1 =>
            val bits     = (x << (missingBits - basePower)).toByte
            val mutatedR = (r | bits).toByte
            fromBasePartial(xs, missingBits - basePower, basePower, result.dropRight(1) :+ mutatedR)

          case -1 if xs.isEmpty =>
            val bitsC    = (byte2Short(x) >> (basePower - missingBits)).toByte
            val mutatedR = (r | bitsC).toByte
            result.dropRight(1) :+ mutatedR

          case -1 =>
            val bitsC    = (byte2Short(x) >> (basePower - missingBits)).toByte
            val bitsN    = ((x & mask(basePower - missingBits)) << (8 - (basePower - missingBits))).toByte
            val mutatedR = (r | bitsC).toByte
            fromBasePartial(xs, 8 - (basePower - missingBits), basePower, result.dropRight(1) :+ mutatedR :+ bitsN)
        end match
  end match
end fromBasePartial

def fromBase[S <: Array[Byte]](
    input: Array[Byte],
    basePower: BasePower
): Array[Byte] =
  fromBasePartial(input, 0, basePower, Array.emptyByteArray)
