package finatra.app.utils

import scala.BigDecimal;
/**
  * Created by jay on 6/1/17.
  */
object TempUtil {

  /**
    * Fahrenheit to Celsius
    * @param f
    * @return
    */
  def FtoC(f: Double): String = {
    val c = (f-32) * 5/9.0;

    val bd = BigDecimal(c.toDouble)
    val result = bd.setScale(2, BigDecimal.RoundingMode.HALF_EVEN)

    result.toString()
  }

  /**
    * Kelvin to Celsius
    * @param f
    * @return
    */
  def KtoC(f: Double): String = {
    val c = f - 273.15

    val bd = BigDecimal(c.toDouble)
    val result = bd.setScale(2, BigDecimal.RoundingMode.HALF_EVEN)

    result.toString()
  }

}
