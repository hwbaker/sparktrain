package com.imooc.spark.kafka

import org.apache.spark.SparkConf
import org.apache.spark.streaming.flume.FlumeUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * SparkStreaming 整合 Flume 的 Pull方式
  */
object FlumePullWordCount {
  def main(args: Array[String]): Unit = {

    if (args.length != 2) {
      System.err.println("usage: FlumePullWordCount <hostname> <port>")
      System.exit(1)
    }

    val Array(hostname, port) = args

    //本地环境联调
    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("FlumePullWordCount")
    //生产环境联调
//    val sparkConf = new SparkConf()//.setMaster("local[2]").setAppName("FlumePullWordCount")

    val ssc = new StreamingContext(sparkConf, Seconds(5))

//    val flumeStream = FlumeUtils.createPollingStream(ssc, "127.0.0.1", 41414)
//    val flumeStream = FlumeUtils.createPollingStream(ssc, "localhost", 41414)
    val flumeStream = FlumeUtils.createPollingStream(ssc, hostname, port.toInt)

    flumeStream.map(x => new String(x.event.getBody.array()).trim)
      .flatMap(_.split(" ")).map((_, 1)).reduceByKey(_+_).print(10)

    ssc.start()
    ssc.awaitTermination()
  }
}
