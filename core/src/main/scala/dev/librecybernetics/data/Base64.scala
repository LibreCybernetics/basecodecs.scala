package dev.librecybernetics.data

opaque type Base64 = String

private val proofSubtype = summon[Base64 <:< String]

object Base64 extends BaseConversions[Base64](codec.Base64):
  given <:<[Base64, String] = proofSubtype
end Base64
