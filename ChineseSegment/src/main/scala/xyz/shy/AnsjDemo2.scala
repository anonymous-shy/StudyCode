package xyz.shy

import org.ansj.domain.Result
import org.ansj.library.StopLibrary
import org.ansj.recognition.impl.StopRecognition
import org.ansj.splitWord.analysis.{BaseAnalysis, DicAnalysis, ToAnalysis}
import org.nlpcn.commons.lang.tire.domain.Forest
import org.nlpcn.commons.lang.tire.library.Library

/**
  * Created by Shy on 2018/6/13
  * Scala Ansj Demo
  */

object AnsjDemo2 extends App {

  val baseAnalysis: Result = BaseAnalysis.parse("孙杨在里约奥运会男子200米自由泳决赛中，以1分44秒65夺得冠。")
  println(s"BaseAnalysis : [$baseAnalysis]")
  val toAnalysis: Result = ToAnalysis.parse("孙杨在里约奥运会男子200米自由泳决赛中，以1分44秒65夺得冠军。")
  println(s"toAnalysis   : [$toAnalysis]")

  val stopRecognition: StopRecognition = StopLibrary.get()
  val toAnalysis2: Result = toAnalysis.recognition(stopRecognition)
  println(s"toAnalysis   : [$toAnalysis2]")

  val forest: Forest = Library.makeForest("library/userLibrary.dic")
  println(forest.getWord("花椒直播").getParams)
  val dicAnalysis: Result = DicAnalysis.parse("孙杨在里约奥运会男子200米自由泳决赛中，以1分44秒65夺得冠军。", forest).recognition(stopRecognition)
  println(s"dicAnalysis  : [$dicAnalysis]")
}
