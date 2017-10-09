package com.dabanshan.user.api.model.request

/**
  * Created by skylai on 2017/9/4.
  */
trait WithUserCreationFields {
  val firstName: String
  val lastName: String
  val email: String
  val username: String
  val password: String
}