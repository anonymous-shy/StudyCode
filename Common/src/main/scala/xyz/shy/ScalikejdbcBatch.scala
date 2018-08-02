package xyz.shy

import scalikejdbc._
import scalikejdbc.config.DBs


/**
  * Created by Shy on 2017/12/29
  */

object ScalikejdbcBatch extends App {

  DBs.setupAll()

  case class User(id: Int, name: String, age: Int)

  case class Member(id: Int, name: String)

  val ls = List[Member](
    Member(11, "aa"),
    Member(12, "bb"),
    Member(13, "cc"))

  val lsi = List[User](
    User(5, "aau", 2),
    User(6, "bbu", 8),
    User(7, "ccu", 9))

  val batchInsertParams = for (el <- ls) yield List(el.id, el.name)
  println(batchInsertParams)


  DB localTx { implicit session =>
    sql"""INSERT INTO test.members (id, name) VALUES (?, ?)""".batch(batchInsertParams: _*).apply()
  }

  val batchUpdateParams = for (el <- lsi) yield List(el.name, el.id)
  //  DB localTx { implicit session =>
  //    sql"""update user set name = ? where id = ?""".batch(batchUpdateParams: _*).apply()
  //  }
  DBs.closeAll()
}
