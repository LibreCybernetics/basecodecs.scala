package dev.librecybernetics.data

opaque type Base32HexLowercase = String

private val proofSubtype = summon[Base32HexLowercase <:< String]

object Base32HexLowercase extends BaseConversions[Base32HexLowercase](codec.Base32HexLowercase):
  given <:<[Base32HexLowercase, String] = proofSubtype
end Base32HexLowercase
