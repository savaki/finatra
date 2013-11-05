package com.twitter.finatra.test

import com.twitter.finatra._
import org.jboss.netty.handler.codec.http.HttpMethod
import com.twitter.util.{Await, Future}
import com.twitter.finagle.http.Response
import com.twitter.finagle.http.{Request => FinagleRequest}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import scala.collection.JavaConverters._
import java.util
import org.jboss.netty.buffer.ChannelBuffers
import java.net.URLEncoder

/**
 * @author matt.ho@gmail.com
 */
trait TestHelper {
  def usingController(controller: Controller): MockApp = MockApp(controller)
}

case class MockApp(controller: Controller) {
  def response = new MockResponse(Await.result(lastResponse))

  private[this] var lastResponse: Future[Response] = null

  def buildRequest(method: HttpMethod, path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map(), body: AnyRef = null): FinagleRequest = {

    // ensure that we don't have both params and body
    if( body != null && !params.isEmpty) {
      throw new RuntimeException("unable to build request.  You can specify EITHER params OR a body, but not BOTH.")
    }

    val request = FinagleRequest(path, params.toList: _*)
    request.httpRequest.setMethod(method)

    // apply body
    for( buffer <- toByteArray(body)) {
      request.setContent(ChannelBuffers.wrappedBuffer(buffer))
      request.addHeader("Content-Length", buffer.length.toString)
    }

    // add headers
    headers.foreach {
      header => request.httpRequest.setHeader(header._1, header._2)
    }

    request
  }

  def execute(method: HttpMethod, path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map(), body: AnyRef = null) {
    val collection = new ControllerCollection
    collection.add(controller)

    val request = buildRequest(method, path, params, headers, body)
    val appService = new AppService(collection)
    val server = new FinatraServer
    val service = server.allFilters(appService)

    lastResponse = service(request)
  }

  /**
   * helper method to url encode a value
   */
  private[finatra] def encode(value: AnyRef) = URLEncoder.encode(value.toString, "UTF-8")

  /**
   * encodes a map into a url form-encoded string
   */
  private[finatra] def encodeFormData(params: Map[AnyRef, AnyRef]): String = {
    params.map {
      case (key, value) => encode(key.toString) + "=" + encode(value.toString)
    }.mkString("&")
  }

  /**
   * intelligently convert an AnyRef into a Array[Byte] using the following rules:
   *
   * <ul>
   *   <li>value: String         => value.getBytes</li>
   *   <li>value: Array[Byte]    => value</li>
   *   <li>value: Map[_, _]      => url-encoded form data</li>
   *   <li>value: util.Map[_, _] => url-encoded form data</li>
   *   <li>value: AnyRef         => uses jackson to attempt to convert this to a json string</li>
   * </ul>
   */
  private[finatra] def toByteArray(body: AnyRef): Option[Array[Byte]] = {
    import MockApp._

    val buffer: Array[Byte] = body match {
      case null => null
      case value: String => value.getBytes
      case value: Array[Byte] => value
      case value: util.Map[_, _] => encodeFormData(value.asInstanceOf[util.Map[AnyRef, AnyRef]].asScala.toMap).getBytes
      case value: Map[_, _] => encodeFormData(value.asInstanceOf[Map[AnyRef, AnyRef]]).getBytes
      case anythingElse => mapper.writeValueAsBytes(anythingElse)
    }
    Option(buffer)
  }

  def get(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map(), body: AnyRef = null) {
    execute(HttpMethod.GET, path, params, headers, body)
  }

  def post(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map(), body: AnyRef = null) {
    execute(HttpMethod.POST, path, params, headers, body)
  }

  def put(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map(), body: AnyRef = null) {
    execute(HttpMethod.PUT, path, params, headers, body)
  }

  def delete(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map(), body: AnyRef = null) {
    execute(HttpMethod.DELETE, path, params, headers, body)
  }

  def head(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map(), body: AnyRef = null) {
    execute(HttpMethod.HEAD, path, params, headers, body)
  }

  def patch(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map(), body: AnyRef = null) {
    execute(HttpMethod.PATCH, path, params, headers, body)
  }

  def options(path: String, params: Map[String, String] = Map(), headers: Map[String, String] = Map(), body: AnyRef = null) {
    execute(HttpMethod.OPTIONS, path, params, headers, body)
  }
}

object MockApp {
  val mapper = {
    val m = new ObjectMapper()
    m.registerModule(DefaultScalaModule)
    m
  }
}

