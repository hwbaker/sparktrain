package com.imooc.spark.kafka.project

import org.apache.spark.SparkConf
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.kafka.KafkaUtils

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
    val ssc = new StringContext(sparkConf, Seconds(60))

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val messages = KafkaUtils.createStream(ssc, zkQuorum, groupId, topicMap)

//    test step1....
    messages.map(_._2).count().print
//    messages.map(_._2).flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    ssc.start()
    ssc.awaitTermination()

  }
}
