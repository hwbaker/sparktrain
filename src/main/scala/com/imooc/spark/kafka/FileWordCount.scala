package com.imooc.spark.kafka

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * tester:
  *   cd /Users/hwbaker/SiteGit/testData/sparkStreaming/
  *   cp ../test.log ./
  */
object FileWordCount {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("FileWordCount").setMaster("local")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val lines = ssc.textFileStream("file:///Users/hwbaker/SiteGit/testData/sparkStreaming/")
    val result = lines.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

    result.print()

    ssc.start()
    ssc.awaitTermination()
  }

}
