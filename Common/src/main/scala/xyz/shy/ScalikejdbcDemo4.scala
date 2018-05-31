package xyz.shy

import scalikejdbc.config.{DBs, DBsWithEnv}
import scalikejdbc._

/**
  * Created by Shy on 2017/12/29
  */

object ScalikejdbcDemo4 extends App {

  // DBs.setup/DBs.setupAll loads specified JDBC driver classes.
  //  DBs.setupAll()
  DBs.setup()
  // DBs.setup('legacy)
  // // Unlike DBs.setupAll(), DBs.setup() doesn't load configurations under global settings automatically
  // DBs.loadGlobalSettings()

  // Use DBsWithEnv instead of DBs.
  //  DBsWithEnv("development").setupAll()
  //  DBsWithEnv("prod").setup('sandbox)

  // loaded from "db.default.*"
  val names: List[String] = DB readOnly { implicit session =>
    sql"SELECT ENAME FROM test.EMP".map(rs => rs.string("ENAME")).list.apply()
  }
  println(names)
  // loaded from "db.legacy.*"
  /*val legacyMemberIds = NamedDB('legacy) readOnly { implicit session =>
    sql"select id from members".map(_.long(1)).list.apply()
  }*/

  // wipes out ConnectionPool
  DBs.closeAll()
}
