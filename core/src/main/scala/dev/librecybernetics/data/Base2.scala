package dev.librecybernetics.data

opaque type Base2 = String

private val proofSubtype = summon[Base2 <:< String]

object Base2 extends BaseConversions[Base2](codec.Base2):
  given <:<[Base2, String] = proofSubtype
end Base2
