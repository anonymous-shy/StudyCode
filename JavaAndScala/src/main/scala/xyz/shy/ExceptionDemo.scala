package xyz.shy

import java.io.{FileInputStream, FileNotFoundException, FileOutputStream, IOException}

import scala.io.Source

/**
  * Created by Shy on 2018/1/25
  */

object ExceptionDemo extends App {

  try {
    Source.fromFile("")
  } catch {
    case ex: FileNotFoundException => ex.printStackTrace()
    case ex: IOException => ex.printStackTrace()
  } finally {

  }
  //如果不关心报出的具体异常类型，捕获
  try {
    Source.fromFile("")
  } catch {
    case t: Throwable => t.printStackTrace()
  }

  var in = None: Option[FileInputStream]
  var out = None: Option[FileOutputStream]
  try {
    in = Some(new FileInputStream(""))
    out = Some(new FileOutputStream(""))
    var c = 0
    while ( {
      c = in.get.read; c != -1
    }) {
      out.get.write(c)
    }
  } finally {
    println("finally closed...")
    if (in.isDefined) in.get.close()
    if (out.isDefined) out.get.close()
  }

  try {
    in = Some(new FileInputStream(""))
    out = Some(new FileOutputStream(""))
    in.foreach { inputStream =>
      out.foreach { outputStream =>
        var c = 0
        while ( {
          c = inputStream.read(); c != -1
        }) {
          outputStream.write(c)
        }
      }
    }
  }
}
