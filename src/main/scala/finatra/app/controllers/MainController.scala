package finatra.app.controllers

import com.twitter.finatra.http.Controller
import com.twitter.finagle.http.Request
import com.twitter.util.Future
import finatra.app.FinatraSwagger
import finatra.app.services.CurrentDateService
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

/**
  * Created by jay <j.milagroso@gmail.com>
  *
  */
class MainController extends Controller {


  // Default page resource (No swagger support)
  get("/") { request: Request =>
    Future(
      compact(
        render(
          List(
            "datetime", CurrentDateService.getWithTime().toString()
          )
        )
      )
    )
  }
}

