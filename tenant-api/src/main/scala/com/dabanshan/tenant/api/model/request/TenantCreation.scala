package com.dabanshan.tenant.api.model.request

import com.dabanshan.commons.regex.Matchers
import com.dabanshan.commons.validation.ValidationViolationKeys._
import com.wix.accord.dsl._
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/4.
  */
case class TenantCreation(
       name: String,
       userId: String,
       address: Option[String],
       phone: Option[String],
       province: Option[String],
       city: Option[String],
       county: Option[String]
)

object TenantCreation {
  implicit val format: Format[TenantCreation] = Json.format

  implicit val tenantCreationValidator = validator[TenantCreation] { u =>

  }
}