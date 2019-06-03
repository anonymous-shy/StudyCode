package xyz.shy.spark220.ml

import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.sql.SparkSession
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.{Matrix, Matrices}
import org.apache.spark.mllib.linalg.distributed.RowMatrix

/**
  * Created by Shy on 2019/5/7
  */

object DataTypesDemo {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName(getClass.getSimpleName)
      .master("local[*]")
      .getOrCreate()

    val dv: Vector = Vectors.dense(1.0, 0.0, 3.0)
    val sv1: Vector = Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0))
    val sv2: Vector = Vectors.sparse(3, Seq((0, 1.0), (2, 3.0)))

    println(s"dv = $dv, sv1 = $sv1, sv2 = $sv2")
    // Create a labeled point with a positive label and a dense feature vector.
    val pos = LabeledPoint(1.0, Vectors.dense(1.0, 0.0, 3.0))
    // Create a labeled point with a negative label and a sparse feature vector.
    val neg = LabeledPoint(0.0, Vectors.sparse(3, Array(0, 2), Array(1.0, 3.0)))
    println(s"a positive label: \n$pos")
    println(s"a negative label: \n$neg")

    //    val examples: RDD[LabeledPoint] = MLUtils.loadLibSVMFile(spark.sparkContext, "data/mllib/sample_libsvm_data.txt")

    // Create a dense matrix ((1.0, 2.0), (3.0, 4.0), (5.0, 6.0))
    val dm: Matrix = Matrices.dense(3, 2, Array(1.0, 3.0, 5.0, 2.0, 4.0, 6.0))
    // Create a sparse matrix ((9.0, 0.0), (0.0, 8.0), (0.0, 6.0))
    /**
      * Creates a column-major sparse matrix in Compressed Sparse Column (CSC) format.
      *
      * @param numRows    number of rows
      * @param numCols    number of columns
      * @param colPtrs    the index corresponding to the start of a new column
      * @param rowIndices the row index of the entry
      * @param values     non-zero matrix entries in column major
      */
    val sm: Matrix = Matrices.sparse(3, 2, Array(0, 1, 3), Array(0, 2, 1), Array(9, 6, 8))
    println(s"a dense matrix: \n$dm")
    println(s"a sparse matrix: \n$sm")

//    val rows: RDD[Vector] =  // an RDD of local vectors
  }
}
