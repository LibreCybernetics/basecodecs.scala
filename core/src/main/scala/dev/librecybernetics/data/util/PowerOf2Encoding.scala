package dev.librecybernetics.data.util

import dev.librecybernetics.data.FnBijection

import scala.annotation.tailrec

def toBase(
    input: Array[Byte],
    basePower: BasePower,
    alphabet: FnBijection[Byte, Char]
): Array[Char] =
  val bits   = input.length * 8
  val result = Array.ofDim[Char](bits / basePower + (if (bits % basePower == 0) 0 else 1))

  @tailrec
  def toBasePartialBase6(offset: Int): Unit =
    val inputOffset = (offset * 6) / 8
    val diff        = input.length - inputOffset
    if (diff > 0) {
      val byte1 = input(inputOffset)
      val byte2 = if (diff > 1) input(inputOffset + 1) else 0.toByte
      val byte3 = if (diff > 2) input(inputOffset + 2) else 0.toByte

      val out1 = ((byte1 >> 2) & 0x3f).toByte
      val out2 = (((byte1 & 0x03) << 4) | ((byte2 >> 4) & 0x0f)).toByte
      result.update(offset, alphabet(out1))
      result.update(offset + 1, alphabet(out2))
      if (diff > 1)
        val out3 = (((byte2 & 0x0f) << 2) | ((byte3 >> 6) & 0x03)).toByte
        result.update(offset + 2, alphabet(out3))
      if (diff > 2)
        val out4 = (byte3 & 0x3f).toByte
        result.update(offset + 3, alphabet(out4))

      // Recurse
      toBasePartialBase6(offset + 4)
    }

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
        result.update(offset, alphabet(bits))
        toBasePartial(offset + 1)
      } else {
        val currentBits =
          (currentByte & mask(remainingBits)) << (basePower - remainingBits)

        val nextBits = if (inputOffset < input.length - 1) {
          byte2Short(input(inputOffset + 1)) >> (8 - basePower + remainingBits)
        } else 0

        val bits = (currentBits | nextBits).toByte

        // Mutate
        result.update(offset, alphabet(bits))
        toBasePartial(offset + 1)
      }
    }
  end toBasePartial

  basePower match
    case 6 => toBasePartialBase6(0)
    case _ => toBasePartial(0)
  end match

  result
end toBase
