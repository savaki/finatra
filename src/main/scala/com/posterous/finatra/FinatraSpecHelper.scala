package com.posterous.finatra.test

import com.posterous.finatra._
import com.capotej.finatra_core.{AbstractFinatraSpec}
import org.jboss.netty.handler.codec.http.DefaultHttpResponse
import com.twitter.util.Future
import scala.collection.mutable.Map

abstract class FinatraSpecHelper extends AbstractFinatraSpec {
  def response = lastResponse.asInstanceOf[FinatraResponse]

  override def buildRequest(method:String, path:String, params:Map[String,String]=Map(), headers:Map[String,String]=Map()) {
    val request   = new Request(method=method,path=path,params=params,headers=headers)
    lastResponse  = app.dispatch(request).asInstanceOf[Option[Future[FinatraResponse]]].get.get()
  }
}