package dev.librecybernetics.data

import scala.annotation.tailrec

def fromBase[S <: Array[Byte]](
    input: Array[Byte],
    basePower: BasePower
): Array[Byte] =
  val result = Array.ofDim[Byte]((input.length * basePower) / 8 + 1)

  @tailrec
  def fromBasePartial(offset: Int): Unit =
    if (offset < input.length) {
      val x            = input(offset)
      val bits         = offset * basePower
      val resultOffset = bits / 8
      val missingBits  = 8 - (bits % 8)
      val r            = result(resultOffset)

      missingBits compare basePower match
        case 0 | 1 =>
          val bits     = (x << (missingBits - basePower)).toByte
          val mutatedR = (r | bits).toByte

          // Mutate and Recurse
          result.update(resultOffset, mutatedR)
          fromBasePartial(offset + 1)

        case -1 if offset >= input.length =>
          val bitsC    = (byte2Short(x) >> (basePower - missingBits)).toByte
          val mutatedR = (r | bitsC).toByte

          // Mutate
          result.update(resultOffset, mutatedR)

        case -1 =>
          val bitsC    = (byte2Short(x) >> (basePower - missingBits)).toByte
          val bitsN    = ((x & mask(basePower - missingBits)) << (8 - (basePower - missingBits))).toByte
          val mutatedR = (r | bitsC).toByte

          // Mutate and Recurse
          result.update(resultOffset, mutatedR)
          result.update(resultOffset + 1, bitsN)
          fromBasePartial(offset + 1)
      end match
    }
  end fromBasePartial

  fromBasePartial(0)

  result.dropRight(1)
