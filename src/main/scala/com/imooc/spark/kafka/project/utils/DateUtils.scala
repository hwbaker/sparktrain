package com.imooc.spark.kafka.project.utils

import java.util.Date

import org.apache.commons.lang3.time.FastDateFormat

/**
  * 日期时间工具类
  */
object DateUtils {
  val YYYYMMDDHHMMSS_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")
  val TARGET_FORMAT = FastDateFormat.getInstance("yyyyMMddHHmmss")

  def getTimes(time: String) = {
    YYYYMMDDHHMMSS_FORMAT.parse(time).getTime
  }

  def parseToMinute(time: String) = {
    TARGET_FORMAT.format(new Date(getTimes(time)))
  }

  def main(args: Array[String]): Unit = {
    println(parseToMinute("2018-02-23 14:34:00"))
  }
}
