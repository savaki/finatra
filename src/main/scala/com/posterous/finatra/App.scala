package com.posterous.finatra

import java.net.InetSocketAddress
import java.util.{NoSuchElementException => NoSuchElement}
import org.jboss.netty.handler.codec.http.HttpMethod._
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import com.twitter.util.Future
import org.jboss.netty.util.CharsetUtil.UTF_8
import com.twitter.finagle.http.{Http, RichHttp, Request, Response}
import com.twitter.finagle.http.Status._
import com.twitter.finagle.http.Version.Http11
import com.twitter.finagle.http.path._
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.builder.{Server, ServerBuilder}


/**
 * @author ${user.name}
 */
object App {

  object NoParams  {
    def unapply(r:Request):Option[Boolean] = {
      if(r.params.toList.isEmpty)
        Some(true)
      else
        None
    }
  }


  object Lol extends ParamMatcher("lol")


//  abstract class NoParamMatcher {
//    def unapply(params: ParamMap) = {
//    }
//  }

  class HelloWorld extends Service[Request, Response]{  

    var routes: Map[String, Function0[Int]] = Map()

    def returnFuture(x:String) = {
      val response = Response(Http11, InternalServerError)
      response.mediaType = "text/plain" 
      response.content = copiedBuffer(x, UTF_8)
      Future.value(response)
    }

    def apply(request: Request) = {
      //j(request.method, Path(request.path) :? NoParams(request)) match {
      //  case (GET, Root / "thing" :? None) => returnFuture("noparams")
      //  case (GET, Root / "thing" :? Lol(lol)) => returnFuture(lol)
      //}
      //println(request.params)
     
      
      
      
      //request match {
      //  case NoParams(_) => returnFuture("no")
      //  case _ => returnFuture("yes")
      //}

      
      //request.method -> (Path(request.path) :? request.params) match {
      //  case GET -> Root => returnFuture("root")
      //  case GET -> Root / "asd" => returnFuture("asd")
      //  case GET -> Root / "asd" / lol => returnFuture(lol)
      //  case GET -> Root / "params" :? Lol(lol) => returnFuture("hey")
      //}
    }
  }

  def main(args : Array[String]) {
    val helloworld = new HelloWorld

    val server: Server = ServerBuilder()
      .codec(RichHttp[Request](Http()))
      .bindTo(new InetSocketAddress(7070))
      .name("helloworld")
      .build(helloworld)

    println("started on 7070")
  }

}

