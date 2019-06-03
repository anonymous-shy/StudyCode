import org.scalatest.FunSuite

/**
  * Created by Shy on 2018/1/15
  */

class Test extends FunSuite {

  test("Str Test") {
    val path = "hdfs://tagticHA/user/shy/data/20news-bydate-train/alt.atheism/49960"
    println(path.split("/").takeRight(2).length)
    println(path.split("/").takeRight(2).head)
  }

  test("double foreach Test") {
    for (i <- 1 to 5; j <- 1 to 3)
      println(s"i = $i, j = $j")
  }
}
