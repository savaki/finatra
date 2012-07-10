package com.posterous.finatra.test

import com.posterous.finatra._
import com.twitter.finatra_core.{AbstractFinatraSpec}
import org.jboss.netty.handler.codec.http.DefaultHttpResponse
import com.twitter.util.Future
import scala.collection.mutable.Map
import org.jboss.netty.handler.codec.http._

abstract class FinatraSpecHelper extends AbstractFinatraSpec[Request, Future[HttpResponse]] {

  def response  = lastResponse.asInstanceOf[Future[HttpResponse]]

  def request = new Request

  var lastResponse:Future[HttpResponse] = null

  def buildRequest(method:String, path:String, params:Map[String,String]=Map(), headers:Map[String,String]=Map()) {
    val req = request
    req.method(method)
    req.path(path)
    req.params(params)
    lastResponse = app.dispatch(req).asInstanceOf[Option[Future[HttpResponse]]].get
  }
}
