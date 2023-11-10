package dev.librecybernetics.data

opaque type Base8 = String

private val proofSubtype = summon[Base8 <:< String]

object Base8 extends BaseConversions[Base8](codec.Base8):
  given <:<[Base8, String] = proofSubtype
end Base8
