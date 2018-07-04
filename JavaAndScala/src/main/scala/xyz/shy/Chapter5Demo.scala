package xyz.shy

import java.time.LocalDateTime

import scala.beans.BeanProperty

/**
  * Created by Shy on 2017/10/9
  * 类
  */

class Counter5 {
  private var value = 0

  val ts = LocalDateTime.now

  def increment() {
    value += 1
  }

  def current = value

  def timestamp = ts
}

class Person5 {
  /**
    * var 生成getter/setter方法
    * val 生成getter方法
    * 将生成4个方法：
    *   1. name:String
    *   2. name_=(newValue: String):Unit
    *   3. getName(): String
    *   4. setName(newValue: String):Unit
    */
  @BeanProperty var name: String = _
  @BeanProperty var age = 0

  override def toString = s"${super.toString} > ${getClass.getSimpleName} > name = $name"
}

class Star5 {
  private var name: String = _
  private var age: Int = 0

  /**
    * 1.辅助构造器的名称 this
    * 2.每一个辅助构造器都必须以一个先前已经定义的其他辅助构造器或主构造器的调用开始
    */
  def this(name: String) {
    this
    this.name = name
  }

  def this(name: String, age: Int) {
    this(name)
    this.age = age
  }
}

/**
  * 私有字段，共有的Scala版getter/setter方法
  */
class Fender(var name: String)

/**
  * 私有字段，共有的Scala版和JavaBeans版的getter/setter方法
  */
class Gibson(@BeanProperty var name: String)

object Chapter5Demo extends App {

  val myCounter = new Counter5
  myCounter.increment()
  println(myCounter.current)
  println(myCounter.timestamp)

  val p = new Person5
  p.setName("AAA")
  p.setAge(1)
  println(s"name: ${p.getName}, age: ${p.getAge}")
  println(p.toString)

  val s1 = new Star5
  val s2 = new Star5("A")
  val s3 = new Star5("A", 1)
}
