package xyz.shy

/**
  * Created by Shy on 2017/9/9.
  */
object StrDemo extends App {

  val s1 =
    """
      |This is
      |a multi line
      |String
    """.stripMargin
  println(s1)
  println("=========")
  val s2 =
    """
      |This is
      |a multi line
      |String
    """.stripMargin.replaceAll("\n", " ")
  println(s2)

  val name = "Shy"
  val age = 1
  println(s"My name is $name")
  println(s"Age is ${age + 1}")
}
