package finatra.app.controllers

import com.twitter.finatra.http.Controller
import finatra.app.requests.NonSecuredRequest

/**
  * Created by jay <j.milagroso@gmail.com>
  */
class JWTController extends Controller {
  // Token generator
  get("/jwt/issue") { request: NonSecuredRequest =>
    // Generates new request token.
  }
}
