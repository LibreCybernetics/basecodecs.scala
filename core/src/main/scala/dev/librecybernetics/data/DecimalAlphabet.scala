package dev.librecybernetics.data

private[data] val DecimalAlphabet: Seq[(Byte, Char)] =
  (0 to 9).map(v => v.toByte -> (v + 48).toChar)
