package com.dabanshan.user.api.model.request

import com.dabanshan.commons.regex.Matchers
import com.dabanshan.commons.validation.ValidationViolationKeys._
import play.api.libs.json.{Format, Json}
import com.wix.accord.dsl._

/**
  * Created by skylai on 2017/9/4.
  */
case class UserCreation(
       firstName: String,
       lastName: String,
       email: String,
       username: String,
       password: String
)extends WithUserCreationFields

object UserCreation {
  implicit val format: Format[UserCreation] = Json.format

  implicit val userCreationValidator = validator[UserCreation] { u =>
    u.firstName as notEmptyKey("firstName") is notEmpty
    u.lastName as notEmptyKey("lastName") is notEmpty
    u.email as matchRegexFullyKey("email") should matchRegexFully(Matchers.Email)
    u.username as notEmptyKey("username") is notEmpty
    u.username as forSizeKey("username") has size > 5
    u.password as notEmptyKey("password") is notEmpty
    u.password as forSizeKey("password")  has size > 7
  }
}