package xyz.shy.spark163.exercise.optimize

/**
  * Created by Shy on 2017/6/15.
  */
object AudienceAnalysis {
  // 生成1000遍历数据
  lazy val nameIndexMap = {
    val nameIndexMap = collection.mutable.HashMap.empty[String, Int]
    val basicName = Seq("first_name", "last_name", "email", "company", "job", "street_address", "city",
      "state_abbr", "zipcode_plus4", "url", "phone_number", "user_agent", "user_name")
    nameIndexMap ++= basicName zip (0 to 12)
    for (i <- 0 to 328) {
      nameIndexMap ++= Seq(("letter_" + i, i * 3 + 13), ("number_" + i, i * 3 + 14), ("bool_" + i, i * 3 + 15))
    }
    nameIndexMap
  }

  def main(args: Array[String]): Unit = {
    println(nameIndexMap.size)
  }
}
