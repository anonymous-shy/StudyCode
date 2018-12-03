package xyz.shy

import scalikejdbc._

/**
  * Created by Shy on 2017/12/29
  * https://www.iteblog.com/archives/1602.html#API-2
  */

object ScalikejdbcDemo1 {

  def main(args: Array[String]): Unit = {
    // initialize JDBC driver & connection pool
    val url = "jdbc:mysql://192.168.71.21:3306/test?useUnicode=true&characterEncoding=UTF8"
    val user = "test001"
    val password = "1q2w3e"
    Class.forName("com.mysql.jdbc.Driver")
    ConnectionPool.singleton(url, user, password)

    // ad-hoc session provider on the REPL
    implicit val session = AutoSession

    // table creation, you can run DDL by using #execute as same as JDBC
    sql"""
      CREATE TABLE members (
        id SERIAL NOT NULL PRIMARY KEY,
        name VARCHAR(64),
        created_at TIMESTAMP NOT NULL
      )
      """.execute.apply()

    // insert initial data
    Seq("Taylor", "AnonYmous") foreach { name =>
      sql"INSERT INTO test.members (name, created_at) VALUES ($name, current_timestamp)".update.apply()
    }

    // for now, retrieves all data as Map value
    val entities: List[Map[String, Any]] = sql"SELECT * FROM test.members".map(_.toMap).list.apply()
    println(entities)

    /*// defines entity object and extractor
    import org.joda.time._
    case class Member(id: Long, name: Option[String], createdAt: DateTime)
    object Member extends SQLSyntaxSupport[Member] {
      override val tableName = "members"
      def apply(rs: WrappedResultSet) = new Member(
        rs.long("id"), rs.stringOpt("name"), rs.jodaDateTime("created_at"))
    }

    // find all members
    val members: List[Member] = sql"select * from test.members".map(rs => Member(rs)).list.apply()

    // use paste mode (:paste) on the Scala REPL
    val m = Member.syntax("m")
    val name = "Alice"
    val alice: Option[Member] = withSQL {
      select.from(Member as m).where.eq(m.name, name)
    }.map(rs => Member(rs)).single.apply()*/
  }
}
