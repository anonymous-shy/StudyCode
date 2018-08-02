package xyz.shy

import scalikejdbc._

/**
  * Created by Shy on 2017/12/29
  */

object ScalikejdbcDemo2 extends App {

  // initialize JDBC driver & connection pool
  val url = "jdbc:mysql://192.168.71.21:3306/test?useUnicode=true&characterEncoding=UTF8"
  val user = "test001"
  val password = "1q2w3e"
  val settings = ConnectionPoolSettings(
    initialSize = 5,
    maxSize = 20,
    connectionTimeoutMillis = 3000L,
    validationQuery = "select 1 from dual")
  Class.forName("com.mysql.jdbc.Driver")
  ConnectionPool.singleton(url, user, password, settings)

  val deptno = "30"
  implicit val session = AutoSession
  val entities: List[Map[String, Any]] = sql"SELECT * FROM test.EMP WHERE DEPTNO = $deptno".map(_.toMap).list.apply()
  println(entities)
  println("*" * 100)

  val empno = 7788

  // simple example
  /*val name: Option[String] = DB readOnly { implicit session =>
    sql"select ENAME from test.EMP where EMPNO = $empno".map(rs => rs.string("ENAME")).single.apply()
  }*/

  // defined mapper as a function
  /*val nameOnly = (rs: WrappedResultSet) => rs.string("ENAME")
  val name: Option[String] = DB readOnly { implicit session =>
    sql"select ENAME from test.EMP where EMPNO = ${empno}".map(nameOnly).single.apply()
  }*/

  // define a class to map the result
  case class Emp(id: String, name: String)

  val emp: Option[Emp] = DB readOnly { implicit session =>
    sql"SELECT EMPNO, ENAME FROM test.EMP WHERE EMPNO = ${empno}"
      .map(rs => Emp(rs.string("EMPNO"), rs.string("ENAME"))).single.apply()
  }
  println(emp)

  /**
    * First Result from Multiple Results
    * first returns the first row of matched rows as an Option value.
    */
  val name: Option[String] = DB readOnly { implicit session =>
    sql"SELECT ENAME FROM test.EMP".map(rs => rs.string("ENAME")).first.apply()
  }

  /**
    * List Results
    * list returns matched multiple rows as scala.collection.immutable.List.
    */
  val names: List[String] = DB readOnly { implicit session =>
    sql"SELECT ENAME FROM test.EMP".map(rs => rs.string("ENAME")).list.apply()
  }

  /**
    * Foreach Operation
    * foreach allows you to make some side-effect in iterations. This API is useful for handling large ResultSet.
    */
  DB readOnly { implicit session =>
    sql"SELECT ENAME FROM test.EMP".foreach { rs =>
      println(rs.string("ENAME"))
    }
  }

  /**
    * Setting JDBC fetchSize
    *
    */
  DB readOnly { implicit session =>
    sql"SELECT ENAME FROM test.EMP"
      .fetchSize(1000)
      .foreach { rs => println(rs.string("ENAME")) }
  }
}
