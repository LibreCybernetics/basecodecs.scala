package dev.librecybernetics.data

import scala.annotation.tailrec

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

def toBase[S <: Array[Byte]](
    input: Array[Byte],
    basePower: BasePower
): Array[Byte] =
  val bits   = input.length * 8
  val result = Array.ofDim[Byte](bits / basePower + 1)

  @tailrec
  def toBasePartial(offset: Int): Unit =
    val bits          = offset * basePower
    val inputOffset   = bits / 8
    val remainingBits = 8 - (bits % 8)

    if (inputOffset < input.length) {
      val x = input(inputOffset)
      remainingBits compare basePower match
        case 0 | 1 => // EQ |GT
          val bits = byte2Short(x) >> (remainingBits - basePower)

          // Mutate
          result.update(offset, (bits.toByte & mask(basePower)).toByte)
          toBasePartial(offset + 1)

        case -1 => // LT
          val bits = toBasePartialByte(x, input.unapply(inputOffset + 1).getOrElse(0), remainingBits, basePower)

          // Mutate
          result.update(offset, bits)
          toBasePartial(offset + 1)
    }
  end toBasePartial

  toBasePartial(offset = 0)

  result.dropRight(if (bits % basePower == 0) 1 else 0)
