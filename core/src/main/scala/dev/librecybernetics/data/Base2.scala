package dev.librecybernetics.data

opaque type Base2 = String

private val proofSubtype = summon[Base2 <:< String]

object Base2:
  given <:<[Base2, String] = proofSubtype

  // Encoding
  given Conversion[Array[Byte], Base2] = codec.Base2.encode(_)
  given Conversion[String, Base2] = Codec.encode(codec.Base2)(_)

  // Decoding
  // NOTE: Due to the opaque type construction, we know decoding won't fail.
  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  given Conversion[Base2, Array[Byte]] = codec.Base2.decode(_).toOption.get
  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  given Conversion[Base2, String] = codec.Base2.decodeString(_).toOption.get
end Base2
