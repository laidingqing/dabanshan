package com.dabanshan.user.api.model.response

import com.dabanshan.commons.response.GeneratedIdDone
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/4.
  */
case class CreationTenantDone(
  id: String
) extends GeneratedIdDone

object CreationTenantDone {
  implicit val format: Format[CreationTenantDone] = Json.format
}