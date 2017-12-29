package xyz.shy

import scalikejdbc._

/**
  * Created by Shy on 2017/12/29
  */

object ScalikejdbcDemo3 extends App {

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

  case class EMP(empno: Int, ename: String, job: String)

  val allColumns = (rs: WrappedResultSet) => EMP(
    empno = rs.int("EMPNO"),
    ename = rs.string("ENAME"),
    job = rs.string("JOB"))

  val users: List[EMP] = DB readOnly { implicit session =>
    sql"""SELECT EMPNO,ENAME,JOB FROM test.EMP""".map(allColumns).list.apply()
  }

  for (user <- users) {
    println(user.empno + "," + user.ename + "," + user.job)
  }
}
