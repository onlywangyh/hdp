package com.wangyh.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SparkCore01_WordCount_Hdfs {

  def main(args: Array[String]): Unit = {
    //创建连接
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc = new SparkContext(sparkConf)

    //执行业务
    val path = "/Users/wangyh/Desktop/Code/Java/hdp/spark-train/spark-core/src/main/resources/data"

    val lines: RDD[String] = sc.textFile(path)
    val word = lines.flatMap(_.split(" "))
    val wordGroup: RDD[(String, Iterable[String])] = word.groupBy(word => word)

    val wordToCount = wordGroup.map {
      case (word, list) => {
        (word, list.size)
      }
    }

    val array: Array[(String, Int)] = wordToCount.collect()
    array.foreach(println)

    //关闭连接
    sc.stop()
  }
}
