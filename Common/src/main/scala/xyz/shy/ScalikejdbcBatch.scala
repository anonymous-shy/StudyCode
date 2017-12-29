package xyz.shy

import scalikejdbc._
import scalikejdbc.config.DBs


/**
  * Created by Shy on 2017/12/29
  */

object ScalikejdbcBatch extends App {

  DBs.setupAll()
  case class User(id: Int, name: String, age: Int)
  val ls = List[User](
    User(5, "aa", 2),
    User(6, "bb", 8),
    User(7, "cc", 9))

  val lsi = List[User](
    User(5, "aau", 2),
    User(6, "bbu", 8),
    User(7, "ccu", 9))

  val batchInsertParams = for (el <- ls) yield List(el.id, el.name, el.age)

  val batchUpdateParams = for (el <- lsi) yield List(el.name, el.id)

  DB localTx { implicit session =>
    sql"""insert into user (id, name, age) values (?, ?, ?)""".batch(batchInsertParams: _*).apply()
  }

  DB localTx { implicit session =>
    sql"""update user set name = ? where id = ?""".batch(batchUpdateParams: _*).apply()
  }
  DBs.closeAll()
}
