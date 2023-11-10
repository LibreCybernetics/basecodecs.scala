package dev.librecybernetics.data

opaque type ZBase32 = String

private val proofSubtype = summon[ZBase32 <:< String]

object ZBase32 extends BaseConversions[ZBase32](codec.ZBase32):
  given <:<[ZBase32, String] = proofSubtype
end ZBase32
