package com.twitter.finatra

import com.twitter.finatra_core.AbstractFinatraController
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http._
import com.twitter.finatra_core._

import scala.collection.mutable.HashSet

class Controller extends AbstractFinatraController[Request, Response] with ResponseBuilder{

  //def render(status:Int = 200, path: String, layout: String = "application.mustache", exports: Any = "") = {
  //  new Response().template(path).layout(layout).status(status).exports(exports).header("Content-Type", "text/html").build
  //}

  override val routes: HashSet[(String, PathPattern, Request => Response)] = HashSet()

  override def get(path: String)   (callback: Request => Response) { addRoute("GET",    path)(callback) }


  override def addRoute(method: String, path: String)(callback: Request => Response) {
    val regex = SinatraPathPatternParser(path)
    routes += ((method, regex, callback))
  }

  override def dispatchRouteOrCallback(request: Request, method: String,
    orCallback: Request => Option[Response]) = {

    findRouteAndMatch(request, method) match {
      case Some((method, pattern, callback)) => Some(callback(request))
      case None => orCallback(request)
    }
  }

  def toJson(obj: Any) = {
    new Response().json(obj).header("Content-Type", "application/json").build
  }

  def redirect(location: String) = Response(301, "moved", Map("Location" -> location))
}
