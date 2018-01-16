package xyz.shy.spark163.ml

import org.apache.spark.{Logging, SparkConf, SparkContext}

/**
  * Created by Shy on 2018/1/12
  */

object TFIDFDemo extends Logging {

  def main(args: Array[String]): Unit = {
    //    Logger.getLogger("org").setLevel(Level.OFF)
    val conf = new SparkConf()
      .setAppName(getClass.getSimpleName)
      .setMaster("local[*]")
    val BASEDIR = "hdfs://tagticHA/user/shy/data/20news-bydate-train/*"
    val sc = new SparkContext(conf)
    val newsData = sc.wholeTextFiles(BASEDIR)
    val newsText = newsData.map { case (file, text) => text }
    newsText.cache
    println(s"Train News Count ===>>> ${newsText.count}")
    val newsGroups = newsData.map { case (file, text) => file.split("/").takeRight(2).head }
    val countByGroup = newsGroups.map((_, 1)).reduceByKey(_ + _).map(x => (x._2, x._1)).sortByKey(ascending = false).map(x => (x._2, x._1))
    println(s"News Group ===>>> ${countByGroup.collect}")
    // 切分文章
    val nonWordSplit = newsText.flatMap(_.split("""\W+""").map(_.toLowerCase))
    println(s"Word Count ===>>> ${nonWordSplit.distinct.count}")
    println(nonWordSplit.distinct.sample(withReplacement = true, 0.3, 42).take(10).mkString(","))
    // 去数字
    val regex =
      """[^0-9]*""".r
    // 使用正则模式过滤掉含有数字的单词
    val filterNumbers = nonWordSplit.filter(token => regex.pattern.matcher(token).matches)
    println(s"Without Nums ===>>> ${filterNumbers.distinct.count}")
    println(filterNumbers.distinct.sample(withReplacement = true, 0.3, 42).take(10).mkString(","))
    // 去除停用词
    val STOP = Set(
      "the", "a", "an", "of", "or", "in", "for", "by", "on", "but", "is", "not",
      "with", "as", "was", "if", "they", "are", "this", "and", "it", "have", "from", "at", "my", "be", "that", "to"
    )
    val stopwords = sc.broadcast[Set[String]](STOP)
    val tokenCounts = filterNumbers.map(t => (t, 1)).reduceByKey(_ + _)
    val oreringDesc = Ordering.by[(String, Int), Int](_._2)
    println(tokenCounts.top(20)(oreringDesc).mkString("\n"))
    val tokenCountsFilteredStopwords = tokenCounts.filter { case (k, v) => !stopwords.value.contains(k) }
    println(tokenCountsFilteredStopwords.top(20)(oreringDesc).mkString("\n"))
    // 删除仅含有一个字符的单词
    val tokenCountsFilteredSize = tokenCountsFilteredStopwords.filter(_._1.length >= 2)
    println(tokenCountsFilteredSize.top(20)(oreringDesc).mkString("\n"))
    // 除去频率低的单词
    /**
      * 很多短语在整个文集中只出现一次。对于使用提取特征来完成的任务,
      * 比如文本相似度比较或者生成机器学习模型,只出现一次的单词是没有价值的,因为这些单词我们没有足够的训练数据。
      */
    val tokenCountsFilteredAll = tokenCountsFilteredSize.filter(_._2 < 2)
    val oreringAsc = Ordering.by[(String, Int), Int](-_._2)
    println(tokenCountsFilteredAll.top(20)(oreringAsc).mkString("\n"))
    println(tokenCountsFilteredAll.count)

    //以上的过滤逻辑可以组合到一个函数中:
    val rareTokens = tokenCounts.filter{ case (k, v) => v < 2 }.map { case (k, v) => k }.collect.toSet
    def tokenize(line : String): Seq[String] = {
      line.split("""\W+""")
        .map(_.toLowerCase)
        .filter(token => regex.pattern.matcher(token).matches)
        .filterNot(token => STOP.contains(token))
        .filterNot(token => rareTokens.contains(token))
        .filter(token => token.length >= 2)
        .toSeq
    }
  }


}
