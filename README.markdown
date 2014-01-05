# Finatra [![Build Status](https://secure.travis-ci.org/twitter/finatra.png)](http://travis-ci.org/twitter/finatra)

[Finatra](http://finatra.info) is a sinatra-inspired web framework for scala, running on top of [Finagle](http://twitter.github.com/finagle/)

See the [Getting Started](http://finatra.info/docs/tutorial.html) guide or the [Documentation](http://finatra.info/docs/index.html) for more information.

```scala
class HelloWorld extends Controller {

  get("/hello/:name") { request =>
    val name = request.routeParams("name").getOrElse("default user")
    render.plain("hello " + name).toFuture
  }

}

object App extends FinatraServer {
  register(new HelloWorld())
}
```

Latest version:

```xml
<dependency>
  <groupId>com.twitter</groupId>
  <artifactId>finatra</artifactId>
  <version>1.4.1</version>
</dependency>
```

## License

Copyright 2013 Twitter, Inc and other contributors

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0

