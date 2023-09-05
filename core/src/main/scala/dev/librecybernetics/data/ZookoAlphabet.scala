package dev.librecybernetics.data

object ZookoAlphabet:
  val zBase32: PFnBijection[Byte, Char] = {
    val Right(mapBijection) = Bijection(
      0.toByte  -> 'y',
      1.toByte  -> 'b',
      2.toByte  -> 'n',
      3.toByte  -> 'd',
      4.toByte  -> 'r',
      5.toByte  -> 'f',
      6.toByte  -> 'g',
      7.toByte  -> '8',
      8.toByte  -> 'e',
      9.toByte  -> 'j',
      10.toByte -> 'k',
      11.toByte -> 'm',
      12.toByte -> 'c',
      13.toByte -> 'p',
      14.toByte -> 'q',
      15.toByte -> 'x',
      16.toByte -> 'o',
      17.toByte -> 't',
      18.toByte -> '1',
      19.toByte -> 'u',
      20.toByte -> 'w',
      21.toByte -> 'i',
      22.toByte -> 's',
      23.toByte -> 'z',
      24.toByte -> 'a',
      25.toByte -> '3',
      26.toByte -> '4',
      27.toByte -> '5',
      28.toByte -> 'h',
      29.toByte -> '7',
      30.toByte -> '6',
      31.toByte -> '9'
    ): @unchecked

    Bijection(
      {
        case byte if byte >= 0 && byte <= 31 => mapBijection(byte).get
      }: PartialFunction[Byte, Char],
      {
        case char if mapBijection.reverse(char).isDefined => mapBijection.reverse(char).get
      }: PartialFunction[Char, Byte]
    )
  }
end ZookoAlphabet
