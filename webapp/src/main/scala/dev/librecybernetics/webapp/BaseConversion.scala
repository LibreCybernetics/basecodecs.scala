package dev.librecybernetics.webapp

import com.raquo.laminar.api.L.*
import org.scalajs.dom.document.getElementById
import org.scalajs.dom.{InputEvent, console}

import dev.librecybernetics.data.{Base64, BaseConversions, Codec, CrockfordBase32}

case class BaseConversion[Base](baseConversion: BaseConversions[Base], plain: Var[String]):
  val encoded: Var[Base] = Var(baseConversion.encodeString(plain.now()))

  val plaintextInput: HtmlElement =
    div(
      label("Plaintext"),
      textArea(
        placeholder := "Plaintext",
        value <-- plain,
        onInput.collect { case ie: InputEvent => ie }.mapToValue.map(baseConversion.encodeString) --> encoded
      )
    )

  val encodedInput: HtmlElement =
    div(
      label("Encoded"),
      textArea(
        placeholder := "Encoded",
        value <-- encoded.signal.map(baseConversion(_)),
        onInput.collect { case ie: InputEvent => ie }.mapToValue.collect { case baseConversion(str) =>
          baseConversion.decodeString(str)
        } --> plain
      )
    )

  val component: HtmlElement =
    div(
      h1(s"Base Conversion ${baseConversion.toString}"),
      plaintextInput,
      encodedInput
    )
end BaseConversion

object BaseConversion:
  val plain: Var[String] = Var("")

  def main(args: Array[String]): Unit =
    val container = getElementById("root")
    render(
      container,
      div(
        plain --> { str =>
          console.debug(str)
        },
        BaseConversion(Base64, plain).component,
        BaseConversion(CrockfordBase32, plain).component
      )
    )
  end main
end BaseConversion
