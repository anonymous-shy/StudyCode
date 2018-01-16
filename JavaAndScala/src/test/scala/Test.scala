import org.scalatest.FunSuite

/**
  * Created by Shy on 2018/1/15
  */

class Test extends FunSuite{

  test("Str Test") {
    val path = "hdfs://tagticHA/user/shy/data/20news-bydate-train/alt.atheism/49960"
    println(path.split("/").takeRight(2).length)
    println(path.split("/").takeRight(2).head)
  }
}
