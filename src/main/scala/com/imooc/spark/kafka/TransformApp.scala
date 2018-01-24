package com.imooc.spark.kafka

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Filter blackList
  */
object TransformApp {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("TransformApp").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    /**
      * build blacklist
      */
    val blacks = List("zs", "ls")
    val map = blacks.map(x => (x, true))
    println(map)

    val blacksRDD = ssc.sparkContext.parallelize(blacks).map(x => (x, true))


    val lines = ssc.socketTextStream("localhost", 6789)
    val clicklog = lines.map(x => (x.split(",")(1), x)).transform(rdd => {
//      rdd.leftOuterJoin(blacksRDD)
      val ss = rdd.leftOuterJoin(blacksRDD)
      ss.filter(c=> c._2._2.getOrElse(false) != true)
        .map(c=>c._2._1)
    })

    clicklog.print()

    ssc.start()
    ssc.awaitTermination()
  }
}
