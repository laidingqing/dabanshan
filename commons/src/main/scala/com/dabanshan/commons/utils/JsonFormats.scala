package com.dabanshan.commons.utils

import play.api.data.validation.ValidationError
import play.api.libs.json._


/**
  * Created by skylai on 2017/8/31.
  */
object JsonFormats {
  def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = Reads {
    case JsString(s) =>
      try {
        JsSuccess(enum.withName(s).asInstanceOf[E#Value])
      } catch {
        case _: NoSuchElementException =>
          JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not contain '$s'")
      }
    case _ => JsError("String value expected")
  }
  def enumWrites[E <: Enumeration]: Writes[E#Value] = Writes(v => JsString(v.toString))
  def enumFormat[E <: Enumeration](enum: E): Format[E#Value] = {
    Format(enumReads(enum), enumWrites)
  }

  def singletonReads[O](singleton: O): Reads[O] = {
    (__ \ "value").read[String].collect(
      ValidationError(s"Expected a JSON object with a single field with key 'value' and value '${singleton.getClass.getSimpleName}'")
    ) {
      case s if s == singleton.getClass.getSimpleName => singleton
    }
  }
  def singletonWrites[O]: Writes[O] = Writes { singleton =>
    Json.obj("value" -> singleton.getClass.getSimpleName)
  }

  def singletonFormat[O](singleton: O): Format[O] = {
    Format(singletonReads(singleton), singletonWrites)
  }
}
