package dev.librecybernetics.data

import scala.annotation.tailrec

def fromBase(
    input: Array[Byte],
    basePower: BasePower
): Array[Byte] =
  val result = Array.ofDim[Byte]((input.length * basePower) / 8)

  @tailrec
  def fromBasePartialBase6(offset: Int): Unit =
    val diff = input.length - offset
    if (diff > 0) {
      val resultOffset = (offset * 6) / 8

      val in1 = input(offset)
      val in2 = input(offset + 1)
      val in3 = if (diff > 2) input(offset + 2) else 0.toByte
      val in4 = if (diff > 3) input(offset + 3) else 0.toByte

      if (diff > 1)
        val byte1 = ((in1 << 2) | ((in2 >> 4) & 0x03)).toByte
        result.update(resultOffset, byte1)
      if (diff > 2)
        val byte2 = ((in2 << 4) | ((in3 >> 2) & 0x0f)).toByte
        result.update(resultOffset + 1, byte2)

      if (diff > 3)
        val byte3 = ((in3 << 6) | (in4 & 0x3f)).toByte
        result.update(resultOffset + 2, byte3)

      // Recurse
      fromBasePartialBase6(offset + 4)
    }

  @tailrec
  def fromBasePartial(offset: Int): Unit =
    if (offset < input.length) {
      val x            = input(offset)
      val bits         = offset * basePower
      val resultOffset = bits / 8
      val missingBits  = 8 - (bits % 8)
      val r            = result(resultOffset)

      if (missingBits >= basePower) {
        val bits     = x << (missingBits - basePower)
        val mutatedR = (r | bits).toByte

        // Mutate and Recurse
        result.update(resultOffset, mutatedR)
        fromBasePartial(offset + 1)
      } else {
        val bitsC    = byte2Short(x) >> (basePower - missingBits)
        val mutatedR = (r | bitsC).toByte

        // Mutate and Recurse
        result.update(resultOffset, mutatedR)
        if (offset < input.length - 1) {
          val bitsN = ((x & mask(basePower - missingBits)) << (8 - (basePower - missingBits))).toByte
          result.update(resultOffset + 1, bitsN)
        }
        fromBasePartial(offset + 1)
      }
    }
  end fromBasePartial

  basePower match
    case 6 => fromBasePartialBase6(0)
    case _ => fromBasePartial(0)
  end match

  result
end fromBase
