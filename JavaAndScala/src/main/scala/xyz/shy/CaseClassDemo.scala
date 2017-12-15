package xyz.shy

/**
  * Created by AnonYmous_shY on 2016/7/22.
  */
object CaseClassDemo extends App {

  def m(h: Human): Unit = {
    h match {
      case Teacher(_) => println("this's a teacher")
      case Stud(_) => println("this's a student")
      case _ => println("unknown")
    }
  }

  m(Stud("Shy"))
}

abstract class Human

case class Teacher(name: String) extends Human

case class Stud(name: String) extends Human

