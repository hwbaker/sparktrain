package com.imooc.spark.kafka

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Spark Streaming处理Socket数据
  *
  * tester: nc -lk 6789
  */
object NetworkWordCount {
  def main(args: Array[String]): Unit = {
    //local[3]:[?] need > 1
    val sparkConf = new SparkConf().setAppName("NetworkWordCount").setMaster("local[3]")

    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val lines = ssc.socketTextStream("localhost", 6789)

    val result = lines.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

    result.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
