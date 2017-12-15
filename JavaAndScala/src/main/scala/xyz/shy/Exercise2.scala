package xyz.shy

/**
  * Created by AnonYmous_shY on 2016/7/22.
  * 抽象类 abstract class
  * 1.类的一个或多个方法没有完整的定义
  * 2.声明抽象类方法不需要加 abstract 关键字，只需不写方法体
  * 3.子类重写父类的抽象方法时不需要加 override 关键字
  * 4.父类可以声明抽象字段(没有初始值的字段)
  * 5.子类重写父类的抽象字段时不需要加 override
  */
class Exercise2 {
}

abstract class Star {
  def sing
}

class Rocker extends Star {
  def sing: Unit = {
    println("Rock")
  }
}

object Exercise2 extends App {


}
