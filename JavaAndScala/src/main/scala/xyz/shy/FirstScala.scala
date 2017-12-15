package xyz.shy

/**
  * Created by AnonYmous_shY on 2016/7/8.
  */
object FirstScala {

  def add(x: Int, y: Int): Int = x + y

  def sayHello(name: String) {
    println("Hello" + name)
  }

  def helloWorld {
    println("Hello World")
  }

  def main(args: Array[String]) {

    //    println(add(1, 2))
    //    sayHello("shy")
    //    helloWorld

    //    val arr = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
    //    for (i <- 0 until arr.length)
    //      println(arr(i))
    //
    //    for (i <- arr)
    //      println(i)

    val map = Map("shy" -> 27, "emma" -> 26, "taylor" -> 27)
    for ((k, v) <- map)
      println("name: " + k + ",age: " + v)
    println(map.keySet)
    println(map.values)
  }
}
