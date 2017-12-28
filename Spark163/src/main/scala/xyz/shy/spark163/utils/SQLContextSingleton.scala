package xyz.shy.spark163.utils

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

/**
  * Created by Shy on 2017/12/28
  */
/** Lazily instantiated singleton instance of SQLContext */
object SQLContextSingleton {
  @transient private var instance: SQLContext = _

  def getInstance(sparkContext: SparkContext): SQLContext = {
    if (instance == null) {
      instance = new SQLContext(sparkContext)
    }
    instance
  }
}
