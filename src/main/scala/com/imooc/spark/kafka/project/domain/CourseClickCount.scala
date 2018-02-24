package com.imooc.spark.kafka.project.domain

/**
  * 实战课程点击数实体类
  * @param day_courseid 对应的HBase中的Rowkey:20171111_1
  * @param click        对应的20171111_1访问总数
  */
case class CourseClickCount(day_courseid:String, click:Long)
