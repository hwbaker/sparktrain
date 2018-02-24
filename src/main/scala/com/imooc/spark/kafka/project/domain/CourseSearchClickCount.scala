package com.imooc.spark.kafka.project.domain

/**
  * 从搜索引擎引流的实战课程点击数实体类
  * @param day_search_courseid 对应的HBase中的Rowkey:20171111_search_1
  * @param click_count        对应的20171111_1访问总数
  */
case class CourseSearchClickCount(day_search_courseid:String, click_count:Long)
