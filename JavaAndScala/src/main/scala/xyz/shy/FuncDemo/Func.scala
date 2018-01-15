package xyz.shy.FuncDemo

object Func extends App {

  // 1.匿名函数
  val sayHelloFunc = (name: String) => println(s"Hello, $name")
  sayHelloFunc("Shy")

  /**
    * 高阶函数的类型推断
    * 高阶函数可以自动推断出参数类型，而不需要写明类型，
    * 而且对于只有一个参数的函数，还可以省区小括号
    * 如果仅有的一个参数在右侧函数体内只使用一次，则还可以将接收参数省略，使用 _ 来替代
    * eg:
    * def greeting(func: (String) => Unit, name: String) {func(name)}
    * greeting((name: String) => println(s"Hello, $name"), "Shy")
    * greeting((name) => println(s"Hello, $name"), "Shy")
    * greeting(name => println(s"Hello, $name"), "Shy")
    */
  // 2.高阶函数 接收其他函数作为参数
  def greeting(func: (String) => Unit, name: String): Unit = {
    func(name)
  }

  greeting(sayHelloFunc, "Dilraba")
  greeting((name: String) => println(s"Hello, $name"), "Shy")
  greeting((name) => println(s"Hello, $name"), "Shy")
  greeting(name => println(s"Hello, $name"), "Shy")

  // 3.高阶函数 函数作为返回值
  def getGreetingFunc(msg: String) = (name: String) => println(s"$msg, $name")

  val greetingFunc = getGreetingFunc("Hey")
  greetingFunc("Emma")

  // 闭包 函数在不处于有效作用域时，还能过对变量进行访问，即为闭包
  // def getGreetingFunc(msg: String) = (name: String) => println(s"$msg, $name")
  // msg局部变量就是闭包

  // SAM装换 ？
  // Currying 函数
  /**
    * 原来接收两个参数的函数变为两个函数
    * 第一个函数接收原来第一个参数，返回接收第二个参数的函数
    * 变为两个函数连续调用的形式
    */
  def sum(a: Int, b: Int) = a + b

  def sum2(a: Int) = (b: Int) => a + b

  def sum3(a: Int)(b: Int) = a + b
  sum3(1)(2)
}
