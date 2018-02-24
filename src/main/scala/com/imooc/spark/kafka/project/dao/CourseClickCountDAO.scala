package com.imooc.spark.kafka.project.dao

import com.imooc.spark.kafka.project.domain.CourseClickCount
import com.imooc.spark.project.utils.HBaseUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

/**
  * 实战课程点击数数据库访问层
  */
object CourseClickCountDAO {

  val tableName = "imooc_course_clickcount"
  val cf = "info"
  val qualifer = "click_count"

  /**
    * 保存数据到HBase
    * @param list CourseClickCount集合
    */
  def save(list: ListBuffer[CourseClickCount]): Unit = {
    val table = HBaseUtils.getInstance().getTable(tableName)

    for (ele <- list) {
      table.incrementColumnValue(Bytes.toBytes(ele.day_courseid),
        Bytes.toBytes(cf),
        Bytes.toBytes(qualifer),
        ele.click_count
      )
    }
  }

  /**
    * 根据Rowkey查询值
    * @param day_courseid
    * @return
    */
  def select(day_courseid: String):Long = {
    val table = HBaseUtils.getInstance().getTable(tableName)

    val gets = new Get(Bytes.toBytes(day_courseid))
    val value = table.get(gets).getValue(cf.getBytes, qualifer.getBytes)

    if (value == null) {
      0L
    } else {
      Bytes.toLong(value)
    }

  }

  def main(args: Array[String]): Unit = {
    var showStr = select("20171111_10") + ":" + select("20171111_7") + ":" + select("20171111_8") + ":" + select("20171111_9")
    println(showStr)

    val list = new ListBuffer[CourseClickCount]


    list.append(CourseClickCount("20171111_10", 10))
    list.append(CourseClickCount("20171111_7", 7))
    list.append(CourseClickCount("20171111_8", 8))
    list.append(CourseClickCount("20171111_9", 9))

    save(list)
    println(select("20171111_10") + ":" + select("20171111_7") + ":" + select("20171111_8") + ":" + select("20171111_9"))
  }
}
