package finatra.app.filters

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Filter, Service}
import com.twitter.util.Future

/**
  * Created by jay <j.milagroso@gmail.com>
  */
class CorsFilter extends Filter[Request, Response, Request, Response] {

  override def apply(request: Request, service: Service[Request, Response]): Future[Response] = {
    service(request).map {
      response =>
        // @TODO Change Base URL.
        response.headerMap.set("Access-Control-Allow-Origin", "http://localhost/")
          .set("Access-Control-Allow-Headers", "Accept, Content-Type")
          .set("Access-Control-Allow-Methods", "GET,POST")
          .set("Content-Security-Policy","default-src 'self';" +
            " style-src 'self' 'unsafe-inline' https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css https://cdnjs.cloudflare.com/ajax/libs/c3/0.4.11/c3.css; " +
            " script-src 'self' 'unsafe-inline' http://d3js.org/d3.v3.min.js https://cdnjs.cloudflare.com/ajax/libs/c3/0.4.11/c3.js;"
          )
          .set("Strict-Transport-Security","max-age=631138519")
          .set("X-Content-Type-Options","nosniff")
          .set("X-Download-Options","noopen")
          .set("X-Frame-Options","sameorigin")
          .set("X-Permitted-Cross-Domain-Policies","none")
          .set("X-Xss-Protection","1; mode=block")
        response
    }
  }
}