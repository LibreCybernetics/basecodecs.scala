package dev.librecybernetics.data

opaque type Base16Lowercase = String

private val proofSubtype = summon[Base16Lowercase <:< String]

object Base16Lowercase extends BaseConversions[Base16Lowercase](codec.Base16Lowercase):
  given <:<[Base16Lowercase, String] = proofSubtype
end Base16Lowercase
