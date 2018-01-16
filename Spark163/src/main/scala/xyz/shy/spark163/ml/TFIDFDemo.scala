package xyz.shy.spark163.ml

import org.apache.spark.{Logging, SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.{SparseVector => SV}
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.feature.IDF

/**
  * Created by Shy on 2018/1/12
  * * 词频-逆文本频率(TF-IDF)
  * TF-IDF给一段文本（叫做文档）中每一个词赋予一个权值。这个权值是基于单词在文本中出现的频率（词频）计算得到的。
  * 同时还要用逆向文本频率做全局的归一化。
  * 逆向文本频率是指单词在所有文档（所有文档的集合对应的数据集通常称为文集）中的频率得到的。
  * tf-idf(t,d) = tf(t,d) * idf(t)
  * 这里tf(t,d)是单词t在文档d中的频率（出现的次数）,idf(t)是文集中单词t的逆向文本频率，
  * idf(t)=log(N/d),N是文档的总数，d是出现过单词t的文档数量
  * TF-IDF的含义是：在一个文档中出现次数很多的词相比出现次数少的词应该在词向量表得到更高的权值。
  * 而IDF归一化起到了弱化在所有文档中总是出现的词的作用。最后的结果是，稀有的或者重要的词被赋予类更高的权重，
  * 而更加常用的单词则在考虑权重的时候有较小的影响。
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
    //    val newsGroups = newsData.map { case (file, text) => file.split("/").takeRight(2).head }
    //    val countByGroup = newsGroups.map((_, 1)).reduceByKey(_ + _).map(x => (x._2, x._1)).sortByKey(ascending = false).map(x => (x._2, x._1))
    //    println(s"News Group ===>>>\n${countByGroup.collect.sortBy(-_._2).mkString("\n")}")
    // 切分文章
    val nonWordSplit = newsText.flatMap(_.split("""\W+""").map(_.toLowerCase))
    //    println(s"Word Count ===>>> ${nonWordSplit.distinct.count}")
    //    println(nonWordSplit.distinct.sample(withReplacement = true, 0.3, 42).take(10).mkString(","))
    // 去数字
    val regex =
    """[^0-9]*""".r
    // 使用正则模式过滤掉含有数字的单词
    val filterNumbers = nonWordSplit.filter(token => regex.pattern.matcher(token).matches)
    //    println(s"Without Nums ===>>> ${filterNumbers.distinct.count}")
    //    println(filterNumbers.distinct.sample(withReplacement = true, 0.3, 42).take(10).mkString(","))
    // 去除停用词
    val STOP = Set(
      "the", "a", "an", "of", "or", "in", "for", "by", "on", "but", "is", "not",
      "with", "as", "was", "if", "they", "are", "this", "and", "it", "have", "from", "at", "my", "be", "that", "to"
    )
    val stopwords = sc.broadcast[Set[String]](STOP)
    val tokenCounts = filterNumbers.map(t => (t, 1)).reduceByKey(_ + _)
    //    val oreringDesc = Ordering.by[(String, Int), Int](_._2)
    //    println(tokenCounts.top(20)(oreringDesc).mkString("\n"))
    val tokenCountsFilteredStopwords = tokenCounts.filter { case (k, v) => !stopwords.value.contains(k) }
    //    println(tokenCountsFilteredStopwords.top(20)(oreringDesc).mkString("\n"))
    // 删除仅含有一个字符的单词
    val tokenCountsFilteredSize = tokenCountsFilteredStopwords.filter(_._1.length >= 2)
    //    println(tokenCountsFilteredSize.top(20)(oreringDesc).mkString("\n"))
    // 除去频率低的单词
    /**
      * 很多短语在整个文集中只出现一次。对于使用提取特征来完成的任务,
      * 比如文本相似度比较或者生成机器学习模型,只出现一次的单词是没有价值的,因为这些单词我们没有足够的训练数据。
      */
    val tokenCountsFilteredAll = tokenCountsFilteredSize.filter(_._2 >= 2)
    val oreringAsc = Ordering.by[(String, Int), Int](-_._2)
    println(tokenCountsFilteredAll.top(20)(oreringAsc).mkString("\n"))
    println(tokenCountsFilteredAll.count)

    //以上的过滤逻辑可以组合到一个函数中:
    val rareTokens = tokenCounts.filter { case (k, v) => v < 2 }.map { case (k, v) => k }.collect.toSet
    def tokenize(line: String): Seq[String] = {
      line.split("""\W+""")
        .map(_.toLowerCase)
        .filter(token => regex.pattern.matcher(token).matches)
        .filterNot(token => STOP.contains(token))
        .filterNot(token => rareTokens.contains(token))
        .filter(token => token.length >= 2)
        .toSeq
    }
    val tokens = newsText.map(doc => tokenize(doc))

    println(tokens.first().take(20))

    ////////--  --////////
    val res = newsText.flatMap(_.split("""\W+""").map(_.toLowerCase))
      .filter(token => regex.pattern.matcher(token).matches)
      .filter { !stopwords.value.contains(_) }
      .map(t => (t, 1)).reduceByKey(_ + _)
      .filter { case (k, v) => k.length >= 2 && v >= 2 }
      .map { case (k, v) => Seq(k) }


    // === train TF-IDF model === //

    //7、训练TF-IDF模型
    val dim = math.pow(2, 18).toInt
    val hashingTF = new HashingTF(dim)
    //HashingTF使用特征哈希把每个输入文本的词映射为一个词频向量的下标
    //每个词频向量的下标是一个哈希值（依次映射到特征向量的某个维度）。词项的值是本身的TF-IDF权重
    //HashingTF的transform函数把每个输入文档（即词项的序列）映射到一个MLlib的Vector对象。
    val tf = hashingTF.transform(tokens)
    //把数据保持在内存中加速之后的操作
    tf.cache()
    val v = tf.first().asInstanceOf[SV]
    println("tf的第一个向量大小:" + v.size)
    //262144
    println("非0项个数：" + v.values.size)
    //706
    println("前10列的下标：" + v.values.take(10).toSeq)
    //WrappedArray(1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 2.0, 1.0, 1.0)
    println("前10列的词频：" + v.indices.take(10).toSeq)
    //WrappedArray(313, 713, 871, 1202, 1203, 1209, 1795, 1862, 3115, 3166)
    //可以看到每一个词频的稀疏向量的维度是262144（2^18）.然而向量中的非0项只有706个。

  }


}
