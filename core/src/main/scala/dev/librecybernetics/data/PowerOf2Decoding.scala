package dev.librecybernetics.data

import scala.annotation.tailrec

def fromBase(
    input: Array[Byte],
    basePower: BasePower
): Array[Byte] =
  val result = Array.ofDim[Byte]((input.length * basePower) / 8)

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

  fromBasePartial(0)

  result
end fromBase
