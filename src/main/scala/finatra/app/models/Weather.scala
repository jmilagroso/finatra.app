package finatra.app.models

/**
  * Created by jay <j.milagroso@gmail.com>
  */

case class Weather(coord: Coordinates, main: Main, wind: Wind, visibility: Int, dt: Long, sys: Sys, id: Int, name: String)

case class Coordinates(lon: Double, lat: Double)

case class Main(temp: Float, pressure: Int, humidity: Int, temp_min: Float, temp_max: Float)

case class Wind(speed: Float, deg: Float)

case class Sys(country: String, sunrise: Long, sunset: Long)