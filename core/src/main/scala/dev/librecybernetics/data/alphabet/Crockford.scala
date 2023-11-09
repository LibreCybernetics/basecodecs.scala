package dev.librecybernetics.data.alphabet

import dev.librecybernetics.data.{Bijection, FnBijection}

object Crockford:
  val base32: FnBijection[Byte, Char] = Bijection(
    {
      case 0  => '0'
      case 1  => '1'
      case 2  => '2'
      case 3  => '3'
      case 4  => '4'
      case 5  => '5'
      case 6  => '6'
      case 7  => '7'
      case 8  => '8'
      case 9  => '9'
      case 10 => 'A'
      case 11 => 'B'
      case 12 => 'C'
      case 13 => 'D'
      case 14 => 'E'
      case 15 => 'F'
      case 16 => 'G'
      case 17 => 'H'
      case 18 => 'J'
      case 19 => 'K'
      case 20 => 'M'
      case 21 => 'N'
      case 22 => 'P'
      case 23 => 'Q'
      case 24 => 'R'
      case 25 => 'S'
      case 26 => 'T'
      case 27 => 'V'
      case 28 => 'W'
      case 29 => 'X'
      case 30 => 'Y'
      case 31 => 'Z'
    },
    _.toUpper match {
      case '0' | 'O'       => 0
      case '1' | 'I' | 'L' => 1

      case '2' => 2
      case '3' => 3
      case '4' => 4
      case '5' => 5
      case '6' => 6
      case '7' => 7
      case '8' => 8
      case '9' => 9
      case 'A' => 10
      case 'B' => 11
      case 'C' => 12
      case 'D' => 13
      case 'E' => 14
      case 'F' => 15
      case 'G' => 16
      case 'H' => 17
      case 'J' => 18
      case 'K' => 19
      case 'M' => 20
      case 'N' => 21
      case 'P' => 22
      case 'Q' => 23
      case 'R' => 24
      case 'S' => 25
      case 'T' => 26
      case 'V' => 27
      case 'W' => 28
      case 'X' => 29
      case 'Y' => 30
      case 'Z' => 31
    }
  )
end Crockford
