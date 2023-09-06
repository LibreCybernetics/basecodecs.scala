package dev.librecybernetics.data

import java.util.Base64
import org.openjdk.jmh.annotations.*

class Base64JavaBench:
  @Benchmark
  def main(): Unit =
    val encoded = Base64.getEncoder.encodeToString(BaseNBench.data1k)
    val decoded = Base64.getDecoder.decode(encoded)
    assert(decoded sameElements BaseNBench.data1k)
end Base64JavaBench
