package com.dabanshan.tenant.api.model.request

import com.wix.accord.dsl._
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/10/9.
  */
case class TenantUpdation(
                           name: String,
                           address: Option[String],
                           phone: Option[String],
                           province: Option[String],
                           city: Option[String],
                           county: Option[String]
                         )

object TenantUpdation {
  implicit val format: Format[TenantUpdation] = Json.format
  implicit val tenantUpdationValidator = validator[TenantUpdation] { u =>

  }
}