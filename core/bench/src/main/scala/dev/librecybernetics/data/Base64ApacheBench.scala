package dev.librecybernetics.data

import org.apache.commons.codec.binary.Base64
import org.openjdk.jmh.annotations.*

import dev.librecybernetics.data.Base64ApacheBench.base64

@State(Scope.Benchmark)
object Base64ApacheBench:
  val base64 = new Base64()
end Base64ApacheBench

class Base64ApacheBench:
  @Benchmark
  def main(): Unit =
    val encoded = base64.encodeToString(BaseNBench.data1k)
    val decoded = base64.decode(encoded)
    assert(decoded sameElements BaseNBench.data1k)
end Base64ApacheBench
