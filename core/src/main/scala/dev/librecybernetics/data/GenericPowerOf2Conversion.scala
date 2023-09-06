package dev.librecybernetics.data

private[librecybernetics] type BasePower = 1 | 3 | 4 | 5 | 6

private[librecybernetics] def blockSize(basePower: BasePower) =
  basePower match
    case 1 => 1
    case 3 => 3
    case 4 => 1
    case 5 => 8
    case 6 => 4
  end match

private[librecybernetics] inline def byte2Short(inline byte: Byte): Short =
  (byte & 0xff).toShort

private[librecybernetics] inline def mask(inline basePower: Int): Byte =
  ((1 << basePower) - 1).toByte
