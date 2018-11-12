//package xyz.shy
//
///**
//  * Created by AnonYmous_shY on 2016/7/22.
//  * 特质 trait -- 对比JAVA8的接口
//  * 1.字段和行为的集合，即 trait中可以包含 字段，抽象方法，完整方法
//  * 2.混入类中
//  * 3.通过with关键字，一个类可以扩展多个特质
//  * 4.trait 可以当做接口，带有具体实现方法的接口，
//  * 5.带有特质的对象，特质从左到右被构造
//  */
//class Exercise3 {
//
//}
//
//trait Logger {
//  def log(msg: String): Unit = {
//    print("log: " + msg)
//  }
//}
//
//class Log1 extends Logger {
//  def logTest: Unit = {
//    log("Test")
//  }
//}
//
///**
//  * Apply
//  */
//class ApplyTest {
//  def test: Unit = {
//    println("test")
//  }
//}
//
////object中为静态方法
//object ApplyTest {
//  def apply = new ApplyTest
//
//  def test: Unit = {
//    println("Test")
//  }
//}