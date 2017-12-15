package xyz.shy

/**
  * Created by AnonYmous_shY on 2016/7/22.
  */
class Exercise1 {
}

object Exercise1 {
  def main(args: Array[String]) {
    val p = new Person("Shy", 27, "male")
    //println(p.name + ":" + p.age + ":" + p.gender)
    val s = new Student("Emma", 26, "Eng")
    println(s.toString)
  }
}

class Person(var name: String, var age: Int) {
  println("This is primary class")
  var gender: String = _

  def this(name: String, age: Int, gender: String) {
    this(name, age)
    this.gender = gender
  }
}

class Student(name: String, age: Int, major: String) extends Person(name, age) {
  println("This is the subclass of Person, major is " + major)

  //父类已有的方法，子类重新定义一定要override
  override def toString = "override toString"
}
