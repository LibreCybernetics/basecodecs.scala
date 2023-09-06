package dev.librecybernetics.data

import scala.annotation.tailrec

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

        val nextBits =
          input.unapply(inputOffset + 1).fold(0.toShort)(byte2Short(_)) >> (8 - basePower + remainingBits)

        val bits = (currentBits | nextBits).toByte

        // Mutate
        result.update(offset, bits)
        toBasePartial(offset + 1)
      }
    }
  end toBasePartial

  toBasePartial(offset = 0)

  result.dropRight(if (bits % basePower == 0) 1 else 0)
end toBase
