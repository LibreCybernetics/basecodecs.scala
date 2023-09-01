package dev.librecybernetics.data

object GenericAlphabet:
  def apply(
      startValue: Byte,
      endValue: Byte,
      startChar: Char,
      endChar: Char,
      offset: Int
  ): PFnBijection[Byte, Char] =
    Bijection(
      {
        case i if i >= startValue && i <= endValue => (i + offset).toChar
      }: PartialFunction[Byte, Char],
      {
        case c if c >= startChar && c <= endChar => (c - offset).toByte
      }: PartialFunction[Char, Byte]
    )
  end apply
end GenericAlphabet
