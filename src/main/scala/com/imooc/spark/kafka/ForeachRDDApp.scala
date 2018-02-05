package com.imooc.spark.kafka

import java.sql.DriverManager

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * tester: nc -lk 6789
  * 统计结果写入到MySql数据库
  */
object ForeachRDDApp {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("ForeachRDDApp")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val lines = ssc.socketTextStream("localhost", 6789)
    val result = lines.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _)

    result.print()


    //Written to MySql。测试失败：org.apache.spark.SparkException: Task not serializable
//    result.foreachRDD( rdd => {
//      val connection = createConnection()
//      rdd.foreach { record =>
//        val sql = "insert into wordcount(word, wordcount) values('" + record._1 + "'," + record._2 + ")"
//        connection.createStatement().execute(sql)
//      }
//    }
//    )

    result.foreachRDD(rdd => {

      rdd.foreachPartition(partitionOfRecords => {

//        if (partitionOfRecords.size > 0) {
          val connection = createConnection()
          partitionOfRecords.foreach(pair => {
            //whether has row or not
            val sqlFind = "select id,word,wordcount from wordcount where word = '" + pair._1 + "'"
            val resultSet = connection.createStatement().executeQuery(sqlFind)
            if(resultSet.next()) {
              val sql = "update wordcount set wordcount = wordcount + " + pair._2 + " where word = '" + pair._1 + "'"
              println("upSql:"+sql+"\n")
              connection.createStatement().execute(sql)
            } else {
              val sql = "insert into wordcount(word, wordcount) values('" + pair._1 + "'," + pair._2 + ")"
              printf("inSql:"+sql+"\n")
              connection.createStatement().execute(sql)
            }
//            while (resultSet.next()) {
//              val id = resultSet.getInt("id")
//              val word = resultSet.getString("word")
//              val wordcount = resultSet.getInt("wordcount")
//              print(id + ":" + word + ":" +wordcount)
//              if (id != 0) {
//                val sql = "update wordcount set wordcount = wordcount + " + pair._2 + " where id = " + id
//                println("update:"+sql)
//                connection.createStatement().execute(sql)
//              } else {
//                val sql = "insert into wordcount(word, wordcount) values('" + pair._1 + "'," + pair._2 + ")"
//                printf("insert:"+sql)
//                connection.createStatement().execute(sql)
//              }
//            }

          })

          connection.close()
//        }
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }

  /**
    * 获取MySQL的连接
    */
  def createConnection() = {
    Class.forName("com.mysql.jdbc.Driver")
    DriverManager.getConnection("jdbc:mysql://localhost:3306/imooc_spark", "root", "12345678")
  }
}
