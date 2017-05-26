package finatra.app.services

import java.text.SimpleDateFormat
import java.util.Date

/**
  * Created by jay <j.milagroso@gmail.com>
  */
object CurrentDateService {
  def get(): String = {
    val currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    currentDate.toString
  }

  def getWithTime(): String = {
    val currentDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());

    currentDate.toString
  }

  def getStartAndEndDates(startDate: String, endDate: String): (String, String) = {
    val currentDate = this.get()
    var sd = startDate
    var ed = endDate
    if(sd=="" && endDate=="") {
      sd = currentDate.concat(" 00:00:00")
      ed = currentDate.concat(" 23:59:59")
    } else {
      sd = if (startDate.contains("00:00:00")) startDate else startDate.concat(" 00:00:00");
      ed = if (endDate.contains("23:59:59"))  endDate else endDate.concat(" 23:59:59");
    }

    (sd, ed)
  }
}
