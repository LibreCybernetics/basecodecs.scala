package dev.librecybernetics.data

opaque type Base32Lowercase = String

private val proofSubtype = summon[Base32Lowercase <:< String]

object Base32Lowercase extends BaseConversions[Base32Lowercase](codec.Base32Lowercase):
  given <:<[Base32Lowercase, String] = proofSubtype
end Base32Lowercase
