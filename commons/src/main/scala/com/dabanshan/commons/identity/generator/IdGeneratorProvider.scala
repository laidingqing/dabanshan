package com.dabanshan.commons.identity.generator

/**
  * Created by skylai on 2017/8/31.
  */
trait IdGeneratorProvider {
  def nextId: Long
}
