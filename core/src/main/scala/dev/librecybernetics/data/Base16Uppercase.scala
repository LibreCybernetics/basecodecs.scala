package dev.librecybernetics.data

opaque type Base16Uppercase = String

private val proofSubtype = summon[Base16Uppercase <:< String]

object Base16Uppercase extends BaseConversions[Base16Uppercase](codec.Base16Uppercase):
  given <:<[Base16Uppercase, String] = proofSubtype
end Base16Uppercase
