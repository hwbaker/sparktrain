package com.imooc.spark.kafka.project.dao

import com.imooc.spark.kafka.project.domain.CourseSearchClickCount
import com.imooc.spark.project.utils.HBaseUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

/**
  * 从搜索引擎引流的实战课程点击数数据库访问层
  */
object CourseSearchClickCountDAO {

  val tableName = "imooc_course_search_clickcount"
  val cf = "info"
  val qualifer = "click_count"

  /**
    * 保存数据到HBase
    * @param list CourseSearchClickCount集合
    */
  def save(list: ListBuffer[CourseSearchClickCount]): Unit = {
    val table = HBaseUtils.getInstance().getTable(tableName)

    for (ele <- list) {
      table.incrementColumnValue(Bytes.toBytes(ele.day_search_courseid),
        Bytes.toBytes(cf),
        Bytes.toBytes(qualifer),
        ele.click_count
      )
    }
  }

  /**
    * 根据Rowkey查询值
    * @param day_search_courseid
    * @return
    */
  def select(day_search_courseid: String):Long = {
    val table = HBaseUtils.getInstance().getTable(tableName)

    val gets = new Get(Bytes.toBytes(day_search_courseid))
    val value = table.get(gets).getValue(cf.getBytes, qualifer.getBytes)

    if (value == null) {
      0L
    } else {
      Bytes.toLong(value)
    }

  }

  def main(args: Array[String]): Unit = {
    var showStr = select("20171111_www.baidu.com_10") + ":" + select("20171111_www.sogou.com_7") +
                  ":" + select("20171111_cn.bing.com_8") + ":" + select("20171111_search.yahoo.com_9")
    println(showStr)

    val list = new ListBuffer[CourseSearchClickCount]


    list.append(CourseSearchClickCount("20171111_www.baidu.com_10", 10))
    list.append(CourseSearchClickCount("20171111_www.sogou.com_7", 7))
    list.append(CourseSearchClickCount("20171111_cn.bing.com_8", 8))
    list.append(CourseSearchClickCount("20171111_search.yahoo.com_9", 9))

    save(list)
    println(select("20171111_www.baidu.com_10") + ":" + select("20171111_www.sogou.com_7") +
      ":" + select("20171111_cn.bing.com_8") + ":" + select("20171111_search.yahoo.com_9"))
  }

}
