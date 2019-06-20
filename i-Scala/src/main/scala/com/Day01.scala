
package com

import org.apache.spark.{SparkConf, SparkContext}

class Day01 {

}
/*
 *运行命令
 *./spark-submit --class com.day01.SparkWC --master spark://bigdata3:7077 --executor-memory 1g --total-executor-cores 2 /home/spark/i-Scala-1.0.jar hdfs://bigdata3:9000/input/air.txt hdfs://bigdata3:9000/output
 */
object  Day01{
  //val logger = Logger(this.getClass)
  def main(args: Array[String]): Unit = {
    //配置信息类
    val conf = new SparkConf().setAppName("SparkWC").setMaster("local[*]")
    //创建上下文
     val sc = new SparkContext(conf)
    //读取数据
    val lines = sc.textFile(args(0))
    //logger.info("啥玩意："+lines)
    val words = lines.flatMap(_.split(","))
    val paired = words.map((_, 1))
    val reduced = paired.reduceByKey(_+_)
    val res = reduced.sortBy(_._2,false)

    //保存位置
    res.saveAsTextFile(args(1))
    println(res.collect().toBuffer)

    //结束任务
    sc.stop()
  }
}

