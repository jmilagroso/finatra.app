package finatra.app.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

/**
  * Created by jay <j.milagroso@gmail.com>
  */
class HTMLController extends Controller {

  // HTML page resource (Serving .html files)
  get("/html") { request: Request =>
    response.ok.file("webapps/index.html")
  }
}
