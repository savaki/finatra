package com.twitter.finatra

trait ResponseBuilder {

  val response = new Response

  def render = this

  def ok = {
    status(200)
    this
  }

  def notFound = {
    status(404)
    this
  }

  def status(s:Int) = {
    this.response.status(s)
    this
  }

  def header(k:String, v:String) = {
    this.response.header(k,v)
    this
  }

  def plain(body:String) = {
    this.header("Content-Type", "text/plain")
    this.response.body(body)
    this
  }

  def html(body:String) = {
    this.header("Content-Type", "text/html")
    this.response.body(body)
    this
  }

  def json(obj:Any) = {
    this.header("Content-Type", "application/json")
    this.response.json(obj)
    this
  }
}
