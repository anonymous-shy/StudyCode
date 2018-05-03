package xyz.shy

/**
  * Created by Shy on 2017/10/7.
  */
object MapDemo extends App {

  val m1 = collection.mutable.Map[String, Int]()
  m1 += ("Shy" -> 1)
  m1 += ("Emma" -> 2)
  m1 += ("Dilraba" -> 3)
  val m2 = new collection.mutable.HashMap[String, Int]
  m2 += ("Dil" -> 1)

  println(m1)
  println(m2)

  m1 foreach {
    case (k, v) => println(s"key: $k, value: $v")
  }

  val grads = Map("Shy" -> 99,
    "Ai" -> 66,
    "Emily" -> 77,
    "Emma" -> 100,
    "Dil" -> 88)

  import collection.immutable.ListMap

  //按键从低到高排序
  val res0 = ListMap(grads.toSeq.sortBy(_._1): _*)
  println(res0)
  //按键从high到low排序
  val res1 = ListMap(grads.toSeq.sortWith(_._1 > _._1): _*)
  println(res1)

  //按value从high到low排序
  val res2 = ListMap(grads.toSeq.sortWith(_._2 > _._2): _*)
  println(res1)

  //查找键或值的max
  grads.max //k max
  grads.keysIterator.max
  grads.keysIterator.reduceLeft((x, y) => if (x > y) x else y)
  grads.keysIterator.reduceLeft((x, y) => if (x.length > y.length) x else y)
  grads.valuesIterator.max


}
