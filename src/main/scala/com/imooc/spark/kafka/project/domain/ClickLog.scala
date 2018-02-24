package com.imooc.spark.kafka.project.domain

/**
  * 清洗后的日志信息
  * @param ip
  * @param time
  * @param courseId
  * @param statusCode
  * @param referer
  */
case class ClickLog(ip:String, time:String, courseId:Int, statusCode:Int, referer:String) {

}
