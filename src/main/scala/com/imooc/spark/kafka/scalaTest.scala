package com.imooc.spark.kafka

/**
  * scala test
  */
object scalaTest {

  def main(args: Array[String]): Unit = {
    val tests = List("2018:aa", "2018:bb")
    val x = tests.map(x => (x.split(":")(1), true))
    val y = tests.map(x => (x.split(":")(1), x))
    val z = tests.map(x => x.split(":")(0).toInt)
    println(x)
    println(y)
    println(z)

    val lines = "a a a b b"
    val result = lines.split(" ").map((_,1))
    println(result)
  }

}
