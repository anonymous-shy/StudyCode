package xyz.shy.spark163.ml

import breeze.linalg._
import breeze.numerics._

/**
  * Created by Shy on 2019/1/10
  */

object BreezeDemo {

  def main(args: Array[String]): Unit = {
    // 1. Breeze 创建向量Vector，矩阵Matrix
    val m1 = DenseMatrix.zeros[Double](2, 3)
    val v1 = DenseVector.zeros[Double](3)
    val v2 = DenseVector.ones[Double](3)
    val v3 = DenseVector.fill(3)(3.0)
    val v4 = DenseVector.range(1, 10, 2)
    val m2 = DenseMatrix.eye[Double](3)
    val m3 = diag(DenseVector(1.0, 2.0, 3.0))
    val m4 = DenseMatrix((1.0, 2.0, 3.0), (4.0, 5.0, 6.0))
    val v5 = DenseVector(1, 2, 3, 4)
    val v6 = DenseVector(1, 2, 3, 4).t
    println(v2)
  }
}
