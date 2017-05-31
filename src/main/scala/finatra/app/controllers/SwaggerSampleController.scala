package finatra.app.controllers

import com.github.xiaodongw.swagger.finatra.SwaggerSupport
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.Future
import finatra.app.FinatraSwagger
import finatra.app.services.CurrentDateService
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

/**
  * Created by jay <j.milagroso@gmail.com>
  */
class SwaggerSampleController extends Controller with SwaggerSupport {
  implicit protected val swagger = FinatraSwagger

  // Secured page resource (With swagger support)
  getWithDoc("/secured") { o =>
    o.summary("Read the detail information about the something")
      .tag("Secured Endpoint")
      .queryParam[String]("token" ,"The token.")
      .queryParam[String]("id" ,"The JWT id.")
      .queryParam[String]("claim" ,"The JWT claim.")
      .queryParam[String]("issuer" ,"The JWT issuer.")
      .queryParam[String]("audience" ,"The JWT audience.")
      .responseWith[String](200, "JSON report")
      .responseWith(200, "Some resource found.")
      .responseWith(404, "Some resource not found.")
  } { request: Request =>
    // Do something.
    Future(
      compact(
        render(
            "datetime", CurrentDateService.getWithTime().toString()
        )
      )
    )
  }
}
