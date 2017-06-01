package finatra.app.controllers

import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.util.concurrent.{LinkedBlockingQueue, ThreadPoolExecutor, TimeUnit, Future => F}

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.{Await, Future, FuturePool}
import com.typesafe.config.{Config, ConfigFactory}
import finatra.app.models.Weather
import finatra.app.utils.{DateUtil, TempUtil}
import finatra.app.views.WeatherView
import org.aredis.cache.{AsyncHandler, RedisCommand, RedisCommandInfo}
import org.asynchttpclient.{AsyncCompletionHandler, AsyncHttpClient, DefaultAsyncHttpClient, Response}
import org.json4s.jackson.JsonMethods._
import org.aredis.cache._



/**
  * Created by jay <j.milagroso@gmail.com>
  */
class AsyncHTTPClientController extends Controller {

  private final val cacheKey = "weather"

  implicit val formats = org.json4s.DefaultFormats

  private val executor: ThreadPoolExecutor = new ThreadPoolExecutor(5, 5, 15, TimeUnit.SECONDS, new LinkedBlockingQueue[Runnable])
  private val f: AsyncRedisFactory = new AsyncRedisFactory(executor)
  // Get a client to DB 1 instead of the default of 0
  private val aredis = f.getClient("localhost/0")

  // Type-safe config
  def config: Config = ConfigFactory.load()

  // Async HTTP Client resource (No swagger support)
  get("/async") { request: Request =>

    val id = request.params.getOrElse("id", "1701668") // Fallback to Manila

    val executor: ThreadPoolExecutor = new ThreadPoolExecutor(5, 5, 15, TimeUnit.SECONDS, new LinkedBlockingQueue[Runnable])
    val f: AsyncRedisFactory = new AsyncRedisFactory(executor)
    // Get a client to DB 1 instead of the default of 0
    val aredis = f.getClient("localhost/0")


    var w:Weather = null


    aredis.submitCommand(new RedisCommandInfo(RedisCommand.GET, cacheKey), new AsyncHandler[RedisCommandInfo]() {

      override def completed(result: RedisCommandInfo, e: Throwable) {
        if (e == null) {
          if (result == null || result.getResult == null) {
            println("no data")
            w = getData(id)
            aredis.sendCommand(RedisCommand.SET, cacheKey, new Gson().toJson(w))
          } else {
            w = new Gson().fromJson(result.getResult.toString, new TypeToken[Weather]() {}.getType)
            println("has data")
          }

          println("data:"+w)
        }
      }
    })

    // @TODO should get data from aredis.submitCommand(new RedisCommandInfo(RedisCommand.GET, cacheKey), new AsyncHandler[RedisCommandInfo]() {



    //futurePool(f.get())

    /*WeatherView(
      weather.id.toString,
      weather.name.toString,
      weather.coord.toString,
      weather.main.humidity.toString,
      weather.main.pressure.toString,
      TempUtil.KtoC(weather.main.temp).toString,
      TempUtil.KtoC(weather.main.temp_min).toString,
      TempUtil.KtoC(weather.main.temp_max).toString,
      weather.visibility.toString,
      weather.wind.speed.toString,
      weather.wind.deg.toString,
      weather.sys.country.toString,
      DateUtil.convert(weather.sys.sunrise).toString,
      DateUtil.convert(weather.sys.sunset).toString,
      DateUtil.convert(weather.dt).toString
    )*/
  }

  def view(weather: Weather): Any = {
    println("view called")
    WeatherView(
       weather.id.toString,
       weather.name.toString,
       weather.coord.toString,
       weather.main.humidity.toString,
       weather.main.pressure.toString,
       TempUtil.KtoC(weather.main.temp).toString,
       TempUtil.KtoC(weather.main.temp_min).toString,
       TempUtil.KtoC(weather.main.temp_max).toString,
       weather.visibility.toString,
       weather.wind.speed.toString,
       weather.wind.deg.toString,
       weather.sys.country.toString,
       DateUtil.convert(weather.sys.sunrise).toString,
       DateUtil.convert(weather.sys.sunset).toString,
       DateUtil.convert(weather.dt).toString
     )
  }

  def getData(id: String):Weather = {
    val asyncHttpClient: AsyncHttpClient = new DefaultAsyncHttpClient
    val s: String = "http://api.openweathermap.org/data/2.5/weather?id="+id+"&APPID="+config.getString("OPENWEATHERMAP.API")
    println(s)
    var ff:F[String] = null
    ff = asyncHttpClient.prepareGet (s).execute (new AsyncCompletionHandler[String] () {
      val bytes: ByteArrayOutputStream = new ByteArrayOutputStream()
      override def onThrowable (t: Throwable) {
        // Something wrong happened.
        logger.error (t.getMessage)
      }

      @throws(classOf[Exception])
      override def onCompleted(response: Response): String = {
        response.getResponseBody(Charset.defaultCharset)
      }
    })

    val map = parse(ff.get())
    val weather = map.extract[Weather]

    weather
  }

}
