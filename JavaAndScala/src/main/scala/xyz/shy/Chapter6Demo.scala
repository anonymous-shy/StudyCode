package xyz.shy

/**
  * Created by Shy on 2017/10/9
  * 对象 object中对象或方法可以直接调用,类似Java中static.
  * 对象本质上可以拥有类的所有特质--它甚至可以扩展类或特质.
  * 只有一个例外：不能提供构造器参数
  * 使用场景：
  * 存放工具函数或常量的地方
  * 高效地共享不可变实例
  *
  */

object Accounts {
  private var newNumber = 0

  private def getNewAccount() = {
    newNumber += 1
    newNumber
  }
}

class Accounts {
  val id = Accounts.getNewAccount()
  private var balance = 0.0

}

//// extends ////
abstract class UndoableAction(val desc: String) {
  def undo

  def redo
}

object DoNothingAction extends UndoableAction("Do Nothing") {
  override def redo: Unit = ???

  override def undo: Unit = ???
}

////////


//// apply ////
class Account private(val id: Int, initBalance: Double) {
  private var balance = initBalance
}

object Account { //伴生对象

  private var id = 0

  private def getNewAccount() = {
    id += 1
    id
  }

  def apply(initBalance: Double): Account =
    new Account(getNewAccount(), initBalance)
}

////////

////枚举////
object LightColor extends Enumeration {
  val Red = Value("Stop")
  val Yellow = Value("Wait")
  val Green = Value("GO")
}

////////
object Chapter6Demo extends App {

  val account = Account(1000.0)
  println(account.id)

  println(LightColor.Green)
  for (c <- LightColor.values)
    println(s"${c.id}: $c")
}
