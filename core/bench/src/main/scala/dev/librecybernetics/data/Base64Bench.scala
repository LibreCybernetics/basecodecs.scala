package dev.librecybernetics.data

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

import org.openjdk.jmh.annotations.Benchmark

class Base64Bench:
  @Benchmark
  def main(): Unit =
    val encoded = Base64.encode(BaseNBench.data1k)
    val decoded = Base64.decode(encoded)
    assert(decoded sameElements BaseNBench.data1k)
end Base64Bench
