package finatra.app

import com.github.xiaodongw.swagger.finatra.{SwaggerController, WebjarsController}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.routing.{FileResolver, HttpRouter}
import finatra.app.controllers._
import io.swagger.models.{Info, Swagger}
import com.typesafe.config.{Config, ConfigFactory}
import finatra.app.filters.{CorsFilter, JWTFilter}

/**
  * Created by jay <j.milagroso@gmail.com>
  */
object App extends Server

object FinatraSwagger extends Swagger

class Server extends HttpServer {

  // Typesafe config
  def conf: Config = ConfigFactory.load()

  // Overrides Server Name
  override def defaultHttpServerName = conf.getString("SERVER.NAME")

  // Overrides Default Finatra HTTP Port
  override def defaultFinatraHttpPort = ":" + conf.getString("SERVER.PORT")

  // Overrides Stream Request
  override def streamRequest = conf.getBoolean("STREAM.REQUEST")

  // Overrides Admin HTTP Server
  override def disableAdminHttpServer = conf.getBoolean("DISABLE.ADMIN.HTTP.SERVER")

  // Swagger
  val finatraSwaggerInfo = new Info()
    .description("Finatra is a framework for easily building API services on top of Finagle and TwitterServer.")
    .version("1.0")
    .title("Finatra App Microservice")
  FinatraSwagger.info(finatraSwaggerInfo)

  // Configure HTTP override.
  override protected def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[CorsFilter]
      .filter[JWTFilter]
      .add(new AsyncHTTPClientController())
      .add(new HTMLController())
      .add(new JWTController())
      .add(new MainController())
      .add(new MustacheController())
      .add(new SwaggerSampleController())
      .add(new SwaggerController(swagger = FinatraSwagger))
      .add(new WebjarsController(new FileResolver("", "")))
    // @TODO inject DB validation before issuance of token.
  }

}
