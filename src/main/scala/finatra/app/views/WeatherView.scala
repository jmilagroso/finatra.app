package finatra.app.views

import com.twitter.finatra.response.Mustache

/**
  * Created by jay <j.milagroso@gmail.com>
  */
@Mustache("weather")
case class WeatherView(
                        id: String,
                        name: String,
                        coordinates: String,
                        humidity: String,
                        pressure: String,
                        temperature: String,
                        temperature_min: String,
                        temperature_max: String,
                        visibility: String,
                        windSpeed: String,
                        windGust: String,
                        country: String,
                        sunrise: String,
                        sunset: String,
                        date: String
                      )