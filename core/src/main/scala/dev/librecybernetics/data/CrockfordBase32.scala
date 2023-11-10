package dev.librecybernetics.data

opaque type CrockfordBase32 = String

private val proofSubtype = summon[CrockfordBase32 <:< String]

object CrockfordBase32 extends BaseConversions[CrockfordBase32](codec.CrockfordBase32):
  given <:<[CrockfordBase32, String] = proofSubtype
end CrockfordBase32
