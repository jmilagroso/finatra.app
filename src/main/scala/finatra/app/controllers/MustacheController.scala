package finatra.app.controllers

import com.twitter.finatra.http.Controller
import finatra.app.requests.SecuredRequest
import finatra.app.services.CurrentDateService
import finatra.app.views.SimpleView

/**
  * Created by jay <j.milagroso@gmail.com>
  */
class MustacheController extends Controller {
  // JWT secured endpoint that requires token
  get("/mustache") { request: SecuredRequest =>
    val (startDate, endDate) =  CurrentDateService.getStartAndEndDates("", "")
    //Future(SimpleView( firstName, lastName ))
    SimpleView(
      startDate, endDate
    )
  }
}
