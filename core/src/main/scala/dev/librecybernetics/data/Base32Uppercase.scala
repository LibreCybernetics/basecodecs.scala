package dev.librecybernetics.data

opaque type Base32Uppercase = String

private val proofSubtype = summon[Base32Uppercase <:< String]

object Base32Uppercase extends BaseConversions[Base32Uppercase](codec.Base32Uppercase):
  given <:<[Base32Uppercase, String] = proofSubtype
end Base32Uppercase
