package com.posterous.finatra

import com.capotej.finatra_core.{AbstractFinatraSpec, FinatraRequest, FinatraController}
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpResponse

abstract class FinatraSpec extends AbstractFinatraSpec {
  def response = lastResponse.asInstanceOf[Future[HttpResponse]]

  override def buildRequest(method:String, path:String, params:Map[String,String]=Map(), headers:Map[String,String]=Map()) {
    val request   = new FinatraRequest(method=method,path=path,params=params,headers=headers)
    lastResponse  = app.dispatch(request).asInstanceOf[Future[HttpResponse]].get
  }
}