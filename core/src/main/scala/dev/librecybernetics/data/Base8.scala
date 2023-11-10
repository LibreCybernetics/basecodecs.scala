package dev.librecybernetics.data

opaque type Base8 = String

private val proofSubtype = summon[Base8 <:< String]

object Base8:
  given <:<[Base8, String] = proofSubtype

  // Encoding
  given Conversion[Array[Byte], Base8] = codec.Base8.encode(_)
  given Conversion[String, Base8]      = codec.Base8.encode(_)

  // Decoding
  // NOTE: Due to the opaque type construction, we know decoding won't fail.
  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  given Conversion[Base8, Array[Byte]] = codec.Base8.decode(_).toOption.get
  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  given Conversion[Base8, String]      = codec.Base8.decodeString(_).toOption.get
end Base8
