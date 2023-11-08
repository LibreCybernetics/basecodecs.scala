package dev.librecybernetics.data

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration

import org.openjdk.jmh.annotations.Benchmark

class Base64Bench:
  @Benchmark
  def main(): Unit =
    val encoded        = Base64.encode(BaseNBench.data1k)
    val Right(decoded) = Base64.decode(encoded): @unchecked
    assert(decoded sameElements BaseNBench.data1k)
end Base64Bench

object Base64Bench:
  private val profilerWait: Boolean = scala.sys.env.get("PROFILE").fold(false)(_ == "true")

  @main
  def main(): Unit =
    if profilerWait then scala.io.StdIn.readLine("Waiting for the profiler to attach...")
    val bench = Base64Bench()
    while true do bench.main()
  end main
end Base64Bench
