package xyz.shy

import scala.beans.BeanProperty

/**
  * Created by Shy on 2017/10/10
  * 继承 extends
  * override
  * 要测试某个对象是否属于某个类,可以用 isInstanceOf 方法
  */

class Employee extends Person5 {
  @BeanProperty var eid: Int = _

  override def toString: String = super.toString + " > " + eid
}

class Epiphone(name: String, val lp: String) extends Gibson(name)

abstract class ESP(val name: String) {
  def id: Int
}

class LTD(name: String) extends ESP(name) {
  override def id = name.hashCode
}

object Chapter7Demo extends App {

  val e = new Employee
  e.setEid(1)
  e.setName("$A")
  e.setAge(11)
  //  println(e.toString)

  if (e.isInstanceOf[Person]) {
    val p = e.asInstanceOf[Person] //p 的类型作为 Person
    println(p.toString)
    if (p.getClass == classOf[Person])
      println("<~~~~~~~~~~~~>")
  }

  if (e.getClass == classOf[Employee])
    println(e.toString)

  val ltd = new LTD("Les Paul")
  println(ltd.id)
}
