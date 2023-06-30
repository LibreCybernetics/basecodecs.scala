package dev.librecybernetics

import scala.annotation.tailrec

private type BasePower = 4 | 5 | 6

private[librecybernetics] def mask(basePower: BasePower): Byte = ((1 << basePower) - 1).toByte

private[librecybernetics] def toBasePartialByte(
    currentByte: Byte,
    nextByte: Byte,
    remainingBits: Int,
    basePower: BasePower
): Byte =
  val bits: Byte = ((currentByte << remainingBits) | (nextByte >> (8 - remainingBits))).toByte
  (bits & mask(basePower)).toByte

@tailrec
private[librecybernetics] def toBasePartial(
    input: Seq[Byte],
    remainingBits: Int,
    basePower: BasePower,
    result: Seq[Byte]
): Seq[Byte] =
  input match
    case Nil => result

    case input @ x :: xs if remainingBits > basePower =>
      val bits = x >> (remainingBits - basePower)
      toBasePartial(input, remainingBits - basePower, basePower, result :+ (bits & mask(basePower)).toByte)

    case x :: xs if remainingBits == basePower =>
      toBasePartial(xs, 8, basePower, result :+ (x & mask(basePower)).toByte)

    case x :: xs if remainingBits < basePower =>
      val bits = toBasePartialByte(x, xs.headOption.getOrElse(0), remainingBits, basePower)
      toBasePartial(xs, 8 - (basePower - remainingBits), basePower, result :+ bits)
  end match

def toBase(input: Seq[Byte], basePower: BasePower): Seq[Byte] =
  toBasePartial(input, 8, basePower, Nil)

@tailrec
private[librecybernetics] def fromBasePartial(
    input: Seq[Byte],
    missingBits: Int,
    basePower: BasePower,
    result: Seq[Byte]
): Seq[Byte] =
  input match
    case Nil => result

    case input @ _ :: _ if missingBits == 0 =>
      fromBasePartial(input, 8, basePower, result :+ 0.toByte)

    case x :: xs if missingBits >= basePower =>
      val bits     = (x << (missingBits - basePower)).toByte
      val r: Byte  = result.last
      val mutatedR = (r | bits).toByte
      fromBasePartial(xs, missingBits - basePower, basePower, result.init :+ mutatedR)

    case x :: xs if missingBits < basePower => ???
  end match

def fromBase(input: Seq[Byte], basePower: BasePower): Seq[Byte] =
  fromBasePartial(input, 0, basePower, Nil)
