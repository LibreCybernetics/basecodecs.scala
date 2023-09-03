package dev.librecybernetics.data

private[librecybernetics] type BasePower = 4 | 5 | 6

private[librecybernetics] inline def byte2Short(inline byte: Byte) =
  if (byte < 0) (byte + 256).toShort else byte.toShort

private[librecybernetics] inline def mask(inline basePower: Int): Byte = ((1 << basePower) - 1).toByte
