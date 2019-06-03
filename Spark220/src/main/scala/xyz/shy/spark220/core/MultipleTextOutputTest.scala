package xyz.shy.spark220.core

import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat
import org.apache.spark.HashPartitioner
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * Created by Shy on 2019/5/30
  */

object MultipleTextOutputTest {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName(getClass.getSimpleName)
      .getOrCreate()

    val data: RDD[(String, String)] = spark.sparkContext.textFile("/user/shy/data/scores.csv")
      //Keyed RDD
      .map(item => (item.split(",").takeRight(2).reverse.mkString("_"), item))
    //按Key Hash分区，4个Key分到4个Partition中
    //      .partitionBy(new HashPartitioner(4))

    /** 按Key保存到不同文件 */
    data.saveAsHadoopFile("/user/shy/data/multiKeyedDir",
      classOf[String],
      classOf[String],
      classOf[PairRDDMultipleTextOutputFormat])
  }
}


/** 继承类重写方法 */
class PairRDDMultipleTextOutputFormat extends MultipleTextOutputFormat[Any, Any] {
  //1)文件名：根据key和value自定义输出文件名。
  override def generateFileNameForKeyValue(key: Any, value: Any, name: String): String = {
    val fileNamePrefix = key.asInstanceOf[String]
    val fileName = fileNamePrefix + "-" + name
    fileName
  }

  //2)文件内容：默认同时输出key和value。这里指定不输出key。
  override def generateActualKey(key: Any, value: Any): String = {
    null
  }
}