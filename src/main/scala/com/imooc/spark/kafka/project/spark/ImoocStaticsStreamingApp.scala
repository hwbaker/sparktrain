package com.imooc.spark.kafka.project.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 使用Spark Streaming 处理Kafka过来的数据
  * localhost:2181 test streamingtopic 1
  */
object ImoocStaticsStreamingApp {
  def main(args: Array[String]): Unit = {
    if (args.length != 4) {
      System.out.println("usage: ImoocStaticsStreamingApp <zkQuorum> <groupId> <topics> <numThreads>")
      System.exit(1)
    }
    val Array(zkQuorum, groupId, topics, numThreads) = args

    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("ImoocStaticsStreamingApp")
    val ssc = new StreamingContext(sparkConf, Seconds(60))

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val messages = KafkaUtils.createStream(ssc, zkQuorum, groupId, topicMap)

//    测试步骤一....
    messages.map(_._2).count().print

    // 测试步骤二
    val log = messages.map(_._2)
    val cleanData = log.map(line => {
      //line => 432.46.156.156	2018-02-23 14:43:00	"GET /class/144.html HTTP/1.1"	200	https://cn.bing.com/search?q=Hadoop基础
      val infos = line.split("\t")
      //infos(2) => GET /class/144.html HTTP/1.1
      //url => /class/144.html
      val url = infos(2).split(" ")(1)
      val courseId = 0

      if (url.startsWith("/class")) {
          val courseIdHtml = url.split("/")(2)
          val courseId = courseIdHtml.substring(0, courseIdHtml.lastIndexOf(".")).toInt
      }

    } )


    ssc.start()
    ssc.awaitTermination()
  }

}
