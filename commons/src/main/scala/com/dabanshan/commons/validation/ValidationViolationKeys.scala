package com.dabanshan.commons.validation

/**
  * Created by skylai on 2017/9/4.
  */
object ValidationViolationKeys {
  def notEmptyKey(field: String) = s"$field.empty"
  def matchRegexFullyKey(field: String) = s"$field.regexNotFullyMatched"
  def forSizeKey(field: String) = s"$field.size"
}
