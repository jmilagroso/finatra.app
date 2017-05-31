package finatra.app.controllers

import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.util.concurrent.{Future => F};

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.Future
import org.asynchttpclient.{AsyncCompletionHandler, AsyncHttpClient, DefaultAsyncHttpClient, Response}
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

/**
  * Created by jay <j.milagroso@gmail.com>
  */
class AsyncHTTPClientController extends Controller {
  // Async HTTP Client resource (No swagger support)
  get("/async") { request: Request =>

    // java future container.
    var f:F[String] = null

    try {
      val asyncHttpClient: AsyncHttpClient = new DefaultAsyncHttpClient
      f = asyncHttpClient.prepareGet ("http://registry.jsonresume.org/jmilagroso.json").execute (new AsyncCompletionHandler[String] () {
        val bytes: ByteArrayOutputStream = new ByteArrayOutputStream();
        override def onThrowable (t: Throwable) {
          // Something wrong happened.
          logger.error (t.getMessage)
        }

        @throws(classOf[Exception])
        override def onCompleted(response: Response): String = {
          response.getResponseBody(Charset.defaultCharset)
        }
      })

      Future(
        compact(
          render(
            f.get()
          )
        )
      )
    } catch {
      case e: Exception => {
        e.printStackTrace ()
      }
    }
  }
}
