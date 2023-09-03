package dev.librecybernetics.data

import scala.annotation.tailrec
import scala.collection.immutable.ArraySeq
import scala.math.max
import scala.util.NotGiven

private[librecybernetics] type BasePower = 4 | 5 | 6

private[librecybernetics] inline def byte2Short(inline byte: Byte) =
  if (byte < 0) (byte + 256).toShort else byte.toShort

private[librecybernetics] inline def mask(inline basePower: Int): Byte = ((1 << basePower) - 1).toByte

private[librecybernetics] inline def toBasePartialByte(
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
    basePower: BasePower,
    result: Array[Byte],
    offset: Int
): Array[Byte] =
  val bits          = offset * basePower
  val inputOffset   = bits / 8
  val remainingBits = 8 - (bits % 8)

  input.unapply(inputOffset) match
    case None => result

    case Some(x) =>
      remainingBits compare basePower match
        case 0 | 1 => // EQ |GT
          val bits = byte2Short(x) >> (remainingBits - basePower)

          // Mutate
          result.update(offset, (bits.toByte & mask(basePower)).toByte)
          toBasePartial(input, basePower, result, offset + 1)

        case -1 => // LT
          val bits = toBasePartialByte(x, input.unapply(inputOffset + 1).getOrElse(0), remainingBits, basePower)

          // Mutate
          result.update(offset, bits)
          toBasePartial(input, basePower, result, offset + 1)
  end match
end toBasePartial

def toBase[S <: Array[Byte]](
    input: Array[Byte],
    basePower: BasePower
): Array[Byte] =
  val bits = input.length * 8

  toBasePartial(
    input,
    basePower,
    Array.fill[Byte](bits / basePower + 1)(0.toByte),
    offset = 0
  ).dropRight(if (bits % basePower == 0) 1 else 0)

@tailrec
private[librecybernetics] def fromBasePartial(
    input: Array[Byte],
    basePower: BasePower,
    result: Array[Byte],
    offset: Int
): Array[Byte] =
  input.unapply(offset) match
    case None => result

    case Some(x) =>
      val bits         = offset * basePower
      val resultOffset = bits / 8
      val missingBits  = 8 - (bits % 8)
      val r            = result.unapply(resultOffset).getOrElse(0.toByte)

      missingBits compare basePower match
        case 0 | 1 =>
          val bits     = (x << (missingBits - basePower)).toByte
          val mutatedR = (r | bits).toByte

          // Mutate
          result.update(resultOffset, mutatedR)
          fromBasePartial(input, basePower, result, offset + 1)

        case -1 if offset >= input.length =>
          val bitsC    = (byte2Short(x) >> (basePower - missingBits)).toByte
          val mutatedR = (r | bitsC).toByte

          // Mutate
          result.update(resultOffset, mutatedR)
          result

        case -1 =>
          val bitsC    = (byte2Short(x) >> (basePower - missingBits)).toByte
          val bitsN    = ((x & mask(basePower - missingBits)) << (8 - (basePower - missingBits))).toByte
          val mutatedR = (r | bitsC).toByte

          // Mutate
          result.update(resultOffset, mutatedR)
          result.update(resultOffset + 1, bitsN)
          fromBasePartial(input, basePower, result, offset + 1)
      end match
  end match
end fromBasePartial

def fromBase[S <: Array[Byte]](
    input: Array[Byte],
    basePower: BasePower
): Array[Byte] =
  fromBasePartial(
    input,
    basePower,
    Array.fill[Byte]((input.length * basePower) / 8 + 1)(0.toByte),
    offset = 0
  ).dropRight(1)
