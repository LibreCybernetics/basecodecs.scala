package dev.librecybernetics.data

import org.scalacheck.Gen

/** Generates a random byte array of length 0-100 with values [0, maxValue]
  *
  * NOTE: Range is inclusive
  */
def randomByteArray(maxValue: Int): Gen[Array[Byte]] = for
  length <- Gen.choose(0, 100)
  input  <- Gen.listOfN(length, Gen.choose(0, maxValue).map(_.toByte)).map(_.toArray)
yield input
