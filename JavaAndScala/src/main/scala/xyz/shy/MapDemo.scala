package xyz.shy

/**
  * Created by Shy on 2017/10/7.
  */
object MapDemo extends App {

  val m1 = collection.mutable.Map[String, Int]()
  m1 += ("Shy" -> 1)
  val m2 = new collection.mutable.HashMap[String, Int]
  m2 += ("Dil" -> 1)
  
  println(m1)
  println(m2)
}
