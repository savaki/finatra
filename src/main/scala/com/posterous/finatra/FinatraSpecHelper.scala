package com.posterous.finatra.test

import com.posterous.finatra._
import com.capotej.finatra_core.{AbstractFinatraSpec}
import org.jboss.netty.handler.codec.http.DefaultHttpResponse
import com.twitter.util.Future
import scala.collection.mutable.Map

abstract class FinatraSpecHelper extends AbstractFinatraSpec {
  def response  = lastResponse.asInstanceOf[FinatraResponse]

  def request(
    path: String,
    method: String = "GET",
    body: Array[Byte] = Array(),
    params: Map[String, String] = Map(),
    multiParams: Map[String, MultipartItem] = Map(),
    headers: Map[String, String] = Map(),
    cookies: Map[String, FinatraCookie] = Map()
  ) = {
    new Request(
      method = method,
      path = path,
      params = params,
      multiParams = multiParams,
      headers = headers,
      cookies = cookies
    )
  }

  override def buildRequest(method:String, path:String, params:Map[String,String]=Map(), headers:Map[String,String]=Map()) {
    val req       = request(method=method,path=path,params=params,headers=headers)
    lastResponse  = app.dispatch(req).asInstanceOf[Option[Future[FinatraResponse]]].get.get()
  }
}
