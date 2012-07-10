package com.posterous.finatra

import scala.collection.mutable.Map
import com.capotej.finatra_core.FinatraRequest

case class Request(
  var path: String,
  var method: String = "GET",
  var body: Array[Byte] = Array(),
  var params: Map[String, String] = Map(),
  var multiParams: Map[String, MultipartItem] = Map(),
  var headers: Map[String, String] = Map(),
  var cookies: Map[String, FinatraCookie] = Map()
) extends FinatraRequest {
  def finatraPath = path
  def finatraMethod = method
  def finatraParams = params
}
