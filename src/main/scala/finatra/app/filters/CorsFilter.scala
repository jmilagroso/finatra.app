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
        response.headerMap.set("Access-Control-Allow-Origin", "*")
          .set("Access-Control-Allow-Headers", "accept, content-type")
          .set("Access-Control-Allow-Methods", "GET,HEAD,POST,DELETE,OPTIONS,PUT,PATCH")
          .set("Content-Security-Policy","default-src 'self' https:; font-src 'self' https: data:; img-src 'self' https: data:; object-src 'none'; script-src https:; style-src 'self' https: 'unsafe-inline'")
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