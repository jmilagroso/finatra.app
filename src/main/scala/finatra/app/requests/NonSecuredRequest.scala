package finatra.app.requests

import com.twitter.finatra.request.QueryParam

/**
  * Created by jay <j.milagroso@gmail.com>
  */
case class NonSecuredRequest(
                           @QueryParam id: String = "",
                           @QueryParam claim: String = "",
                           @QueryParam issuer: String = "",
                           @QueryParam audience: String = ""
                         )