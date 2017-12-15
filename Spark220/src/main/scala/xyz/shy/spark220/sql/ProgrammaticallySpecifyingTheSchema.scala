package xyz.shy.spark220.sql

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

/**
  * Created by Shy on 2017/3/22.
  */
object ProgrammaticallySpecifyingTheSchema extends App {

  val spark = SparkSession
    .builder()
    .appName("ProgrammaticallySpecifyingTheSchema")
    .master("local")
    //    .config("spark.sql.warehouse.dir", "F:\\spark-warehouse")
    .getOrCreate()

  // Create an RDD
  val peopleRDD = spark.sparkContext.textFile("/test/resources/people.txt")

  // The schema is encoded in a string
  //  val schemaString = "name age"

  // Generate the schema based on the string of schema
  //  val fields = schemaString.split(" ")
  //    .map(fieldName => StructField(fieldName, StringType, nullable = true))
  //  val schema = StructType(fields)

  val schema = StructType(Array(
    StructField("name", StringType),
    StructField("age", IntegerType)
  ))

  // Convert records of the RDD (people) to Rows
  val rowRDD = peopleRDD
    .map(_.split(","))
    .map(attributes => Row(attributes(0), attributes(1).trim.toInt))

  // Apply the schema to the RDD
  val peopleDF = spark.createDataFrame(rowRDD, schema)
  peopleDF.printSchema()

  // Creates a temporary view using the DataFrame
  peopleDF.createOrReplaceTempView("people")

  // SQL can be run over a temporary view created using DataFrames
  val results = spark.sql("SELECT * FROM people")

  // The results of SQL queries are DataFrames and support all the normal RDD operations
  // The columns of a row in the result can be accessed by field index or by field name
  //  results.map(attributes => "Name: " + attributes(0)).show()
  results.show()
  //  +-------+---+
  //  |   name|age|
  //  +-------+---+
  //  |Michael| 29|
  //  |   Andy| 30|
  //  | Justin| 19|
  //  +-------+---+
}
