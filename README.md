# finatra.app
A Finatra Web Application that implements Swagger, JWT, Async HTTP Client and TypeSafe configuration.

##### Resources

This Finatra Web App uses a number of open source projects to work properly:

* [Finatra](https://twitter.github.io/finatra/) - Fast, testable, Scala services built on [TwitterServer](https://twitter.github.io/twitter-server/) and [Finagle](https://twitter.github.io/finagle/).
* [Swagger-Finatra](https://github.com/xiaodongw/swagger-finatra) - Finatra Swagger Support
* [Typesafe Config](https://github.com/typesafehub/config) - Configuration library for JVM languages
* [Async HTTP Client](https://github.com/AsyncHttpClient/async-http-client) - Asynchronous Http and WebSocket Client library for Java
* [JWT](https://github.com/auth0/java-jwt) - Java implementation of JSON Web Token (JWT)

##### VM Options
```sh
-Dconfig.file=src/main/resources/finatra.app.conf
```

 
##### Program Arguments
```sh
 -local.doc.root=src/main/resources/
 ```

##### Configurations
```sh
SERVER.NAME = "Finatra App"
SERVER.PORT = 40001
STREAM.REQUEST = false
DISABLE.ADMIN.HTTP.SERVER = true
DEFAULT.TRACING.ENABLED = false
JWT.SECRET = "a unique set of custom, high quality, cryptographic-strength password strings (64 random hexadecimal characters, 63 random printable ASCII characters, 
63 random alpha-numeric characters, etc)"
JWT.EXPIRES.DAYS = 1
```

##### Examples
```sh
* Async HTTP Client example -> finatra.app.controllers.AsyncHTTPClientController
* JWT example-> {finatra.app.controllers.JWTController, finatra.app.filters.JWTFilter}
* Mustache example -> finatra.app.controllers.MustacheController
* Serve HTML file example -> finatra.app.controllers.HTMLController
* Swagger Integration  example -> finatra.app.controllers.SwaggerSampleController
```

##### License
This code is distributed using the [Apache license, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
