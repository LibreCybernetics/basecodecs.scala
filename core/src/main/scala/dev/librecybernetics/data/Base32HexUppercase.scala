package dev.librecybernetics.data

opaque type Base32HexUppercase = String

private val proofSubtype = summon[Base32HexUppercase <:< String]

object Base32HexUppercase extends BaseConversions[Base32HexUppercase](codec.Base32HexUppercase):
  given <:<[Base32HexUppercase, String] = proofSubtype
end Base32HexUppercase
