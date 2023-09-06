package dev.librecybernetics.data

import scala.annotation.tailrec

def toBase(
    input: Array[Byte],
    basePower: BasePower
): Array[Byte] =
  val bits   = input.length * 8
  val result = Array.ofDim[Byte](bits / basePower + (if (bits % basePower == 0) 0 else 1))

  @tailrec
  def toBasePartial(offset: Int): Unit =
    val bits          = offset * basePower
    val inputOffset   = bits / 8
    val remainingBits = 8 - (bits % 8)

    if (inputOffset < input.length) {
      val currentByte = input(inputOffset)

      if (remainingBits >= basePower) {
        val bits =
          ((byte2Short(currentByte) >> (remainingBits - basePower)) & mask(basePower)).toByte

        // Mutate
        result.update(offset, bits)
        toBasePartial(offset + 1)
      } else {
        val currentBits =
          (currentByte & mask(remainingBits)) << (basePower - remainingBits)


        val nextBits = if (inputOffset < input.length - 1) {
          byte2Short(input(inputOffset + 1)) >> (8 - basePower + remainingBits)
        } else 0

        val bits = (currentBits | nextBits).toByte

        // Mutate
        result.update(offset, bits)
        toBasePartial(offset + 1)
      }
    }
  end toBasePartial

  toBasePartial(offset = 0)

  result
end toBase
