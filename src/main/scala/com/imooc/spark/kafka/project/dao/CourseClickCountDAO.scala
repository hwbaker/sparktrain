package com.imooc.spark.kafka.project.dao

import com.imooc.spark.kafka.project.domain.CourseClickCount

import scala.collection.mutable.ListBuffer

/**
  * 实战课程点击数数据库访问层
  */
case class CourseClickCountDAO() {

  val tableName = "imooc_course_clickcount"
  val cf = "info"
  val qualifer = "click_count"

  /**
    * 保存数据到HBase
    * @param list CourseClickCount集合
    */
  def save(list: ListBuffer[CourseClickCount]): Unit = {

  }

  /**
    * 根据Rowkey查询值
    * @param day_courseid
    * @return
    */
  def select(day_courseid: String):Long = {
    0l
  }


}
