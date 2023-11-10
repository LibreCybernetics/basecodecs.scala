package dev.librecybernetics.data

trait BaseConversions[Base](codec: Codec)(using eqv: =:=[Base, String]) {
  given encode: Conversion[Array[Byte], Base] = bytes => eqv.flip(codec.encode(bytes))

  given encodeString: Conversion[String, Base] = str => eqv.flip(codec.encode(str))

  // Decoding
  // NOTE: Due to the opaque type construction, we know decoding won't fail.
  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  given decode: Conversion[Base, Array[Byte]] = base => codec.decode(eqv(base)).toOption.get

  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  given decodeString: Conversion[Base, String] = base => codec.decodeString(eqv(base)).toOption.get
}
