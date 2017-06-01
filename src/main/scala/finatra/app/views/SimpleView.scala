package finatra.app.views

import com.twitter.finatra.response.Mustache

/**
  * Created by jay <j.milagroso@gmail.com>
  */
@Mustache("simple")
case class SimpleView(first: String, second: String)

