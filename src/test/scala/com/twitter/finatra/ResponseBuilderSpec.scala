package com.twitter.finatra.test

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.jboss.netty.util.CharsetUtil.UTF_8

import com.twitter.finatra.ResponseBuilder

class MockController extends ResponseBuilder {}

@RunWith(classOf[JUnitRunner])
class ResponseBuilderSpec extends ShouldSpec {
  def controller = new MockController

  "render.ok" should "return a 200 response" in {
    controller.render.ok.response.status should equal (200)
  }

  "render.notFound" should "return a 404 response" in {
    controller.render.notFound.response.status should equal (404)
  }

  "render.status(201)" should "return a 201 response" in {
    controller.render.status(201).response.status should equal (201)
  }

  "render.plain()" should "return a 200 plain response" in {
    val response = controller.render.plain("howdy").response

    response.status should equal (200)
    response.strBody.get should equal ("howdy")
    response.headers("Content-Type") should equal ("text/plain")
  }

  "render.html()" should "return a 200 html response" in {
    val response = controller.render.html("<h1>howdy</h1>").response

    response.status should equal (200)
    response.strBody.get should equal ("<h1>howdy</h1>")
    response.headers("Content-Type") should equal ("text/html")
  }

  "render.json()" should "return a 200 json response" in {
    val response = controller.render.json(Map("foo" -> "bar")).response
    val body     = response.build.get.getContent.toString(UTF_8)

    response.status should equal (200)
    body should equal ("""{"foo":"bar"}""")
    response.headers("Content-Type") should equal ("application/json")
  }

}
