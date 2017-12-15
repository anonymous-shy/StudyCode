package xyz.shy

import scala.collection.mutable

/**
  * Created by Shy on 2017/10/10.
  * 集合
  * 1.所有集合都扩展自Iterable
  * 2.集合三大类：序列，集，映射
  * 3.几乎所有集合类，都提供了可变和不可变的版本
  * 4.列表要么为空，要么拥有一头一尾，尾部本身又是个列表
  * 5.集是无先后次序的集合
  * 6.用LinkedHashSet来保留插入顺序，或用SortedSet来按顺序进行迭代
  * 7. + - +: :+ ++ -- 的使用
  * 8.映射，折叠，拉链操作
  */
/**
  * Seq 一个有先后次序的值的序列，比如数组或列表。(sequence)
  * Set 一组没有先后次序的值，
  * Map 一组<k,v>键值对
  */
/**
  * 1.Seq序列
  * 不可变列表：List，Stream，Stack，Queue，Vector，Range
  * Vector是ArrayBuffer的不可变版本：一个带下标的序列，支持快速的随机访问。
  * Range表示一个整数序列
  * 可变列表：ArrayBuffer，Stack，Queue，Priority Queue，LinkedList(可变List选择只有LinkedList,ListBuffer)
  * 其中，栈，队列，优先级队列都是标准的数据结构，用来实现特定的算法。
  */
/**
  *
  */
object Chapter13Demo extends App {

  /**
    * 列表
    * Scala中，列表要么是 Nil(空表)，要么是一个head元素加上一个tail，而tail又是一个列表。
    */
  val l = List(2, 4)
  // :: 操作符从给定的头和尾创建一个新的列表
  // 注意： :: 是右结合的，列表从末端开始构建
  9 :: l //9 :: 4 :: 2 :: Nil

  val ab = mutable.ArrayBuffer[Int]()
  ab += (1, 3, 5, 7)

  val al = mutable.ListBuffer[String]()
  al += "Shy"
  al += (" Love ", "Dilraba")
  println(al.mkString)
  /**
    * Set: 不重复不保留插入的顺序
    */
  val s = mutable.Set()
}
