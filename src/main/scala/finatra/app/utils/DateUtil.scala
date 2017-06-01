package finatra.app.utils

import java.sql.Date
import java.text.SimpleDateFormat

/**
  * Created by jay on 6/1/17.
  */
object DateUtil {

  def convert(dt: Long) : String = {
    val d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      .format(new Date(dt * 1000L))

    d
  }

}
