package com.posterous.finatra.test

import com.posterous.finatra.FinatraApp
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.jboss.netty.util.CharsetUtil.UTF_8

class MyApp extends FinatraApp {
  get("/path") { request => response("get:path") }
  // post("/path") { request => "post:path" }
  // put("/path") { request => "put:path" }
  // delete("/path") { request => "delete:path" }
  // patch("/path") { request => "patch:path" }
  // get("/params") { request => request.params("p") }
  // get("/headers") { request => request.headers("Referer") }
}

@RunWith(classOf[JUnitRunner])
class FinatraIntegrationSpec extends FinatraSpecHelper {

  def app = { new MyApp }

  "GET /path" should "respond 200" in {
    get("/path")
    val thing = lastResponse.get.getContent.toString(UTF_8)
    thing should equal ("get:path")
  }

  // "POST /path" should "respond 200" in {
  //   post("/path")
  //   lastResponse should equal (Some("post:path"))
  // }

  // "PUT /path" should "respond 200" in {
  //   put("/path")
  //   lastResponse should equal (Some("put:path"))
  // }

  // "DELETE /path" should "respond 200" in {
  //   delete("/path")
  //   lastResponse should equal (Some("delete:path"))
  // }

  // "PATCH /path" should "respond 200" in {
  //   patch("/path")
  //   lastResponse should equal (Some("patch:path"))
  // }

  // "GET /params" should "respond 200" in {
  //   get("/params", Map("p"->"yup"))
  //   lastResponse should equal (Some("yup"))
  // }

  // "GET /headers" should "respond 200" in {
  //   get("/headers", headers=Map("Referer"->"http://twitter.com"))
  //   lastResponse should equal (Some("http://twitter.com"))
  // }

}
