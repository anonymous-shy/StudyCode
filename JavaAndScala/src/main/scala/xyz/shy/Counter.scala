package xyz.shy

/**
  * Created by Shy on 2017/10/8.
  */
class Counter {

  private var value = 0

  // 对改值器使用 () 即改变对象状态使用 ()
  def increment() {
    value += 1
  }

  // 而对于取值器去掉 () 定义时如果没有参数就不需要加(),这样使用必须通过 myCounter.current
  def current = value
}

object myCounter extends App {
  val myCounter = new Counter
  myCounter.increment()
  println(myCounter.current)
}
