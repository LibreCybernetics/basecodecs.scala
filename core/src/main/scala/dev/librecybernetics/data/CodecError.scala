package dev.librecybernetics.data

enum CodecError extends Throwable:
  case UnrecognizedChar(char: Char)
end CodecError
