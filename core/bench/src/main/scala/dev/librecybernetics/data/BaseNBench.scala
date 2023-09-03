package dev.librecybernetics.data

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Random

import org.openjdk.jmh.annotations.*

@State(Scope.Benchmark)
object BaseNBench:
  val codecs = Set(
    Base16Lowercase,
    Base16Uppercase,
    Base32HexLowercase,
    Base32HexUppercase,
    Base32Lowercase,
    Base32Uppercase,
    Base64,
    Base64URLSafe
  )

  val rand = Random()
  val data = rand.nextBytes(8 * 1024)
end BaseNBench

class BaseNBench:
  @Benchmark
  def main(): Unit =
    given ExecutionContext = ExecutionContext.global

    def futures() = BaseNBench.codecs.map { codec =>
      Future {
        val encoded = codec.encode(BaseNBench.data)
        val decoded = codec.decode(encoded)
        assert(decoded sameElements BaseNBench.data)
      }
    }

    // Wait for all to finish
    def all(): Future[Set[Unit]] = Future.sequence(futures())
    Await.result(all(), Duration.Inf)
end BaseNBench
