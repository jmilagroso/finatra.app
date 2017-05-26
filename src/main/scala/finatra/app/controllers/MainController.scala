package finatra.app.controllers

import com.twitter.finatra.http.Controller
import com.github.xiaodongw.swagger.finatra.SwaggerSupport
import com.twitter.finagle.http.{Request}
import com.twitter.util.Future
import finatra.app.FinatraSwagger
import finatra.app.services.{CurrentDateService}
import finatra.app.views.SimpleView
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

/**
  * Created by jay <j.milagroso@gmail.com>
  *
  * /api-docs/ui
  * /api-docs/model
  */
class MainController extends Controller with SwaggerSupport {
  implicit protected val swagger = FinatraSwagger

  // Default page resource (No swagger support)
  get("/") { request: Request =>
    Future(
      compact(
        render(
          List(
            "resource", "/",
            "datetime", CurrentDateService.getWithTime().toString(),
            "token",    ""
          )
        )
      )
    )
  }

  // User page resource (With swagger support)
  getWithDoc("/user") { o =>
    o.summary("Read the detail information about the user")
      .tag("User Endpoint")
      .queryParam[String]("token" ,"The security token.")
      .responseWith[String](200, "JSON report")
      .responseWith(200, "User resource found.")
      .responseWith(404, "User resource not found.")
  } { request: Request =>
    Future(
      compact(
        render(
          List(
            "resource", "user",
            "datetime", CurrentDateService.getWithTime().toString(),
            "token",    request.params.get("token").get
          )
        )
      )
    )
  }

  // Mustache page resource (No swagger support)
  get("/mustache") { request: Request =>
    var (startDate, endDate) =  CurrentDateService.getStartAndEndDates("", "")
    //Future(SimpleView( firstName, lastName ))
    SimpleView(
      startDate, endDate
    )
  }

  // HTML page resource (Serving .html files)
  get("/html") { request: Request =>
    response.ok.file("webapps/index.html")
  }


}
