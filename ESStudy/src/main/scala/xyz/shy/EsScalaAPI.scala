package xyz.shy

import java.net.InetAddress

import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.transport.client.PreBuiltTransportClient

/**
  * Created by Shy on 2017/11/14
  */

object EsScalaAPI extends App {

  val settings = Settings.builder
    .put("cluster.name", "tagtic-es-cluster")
    .build
  val client = new PreBuiltTransportClient(settings)
    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("spider_slave05"), 9300))
    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("spider_slave06"), 9300))
    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("spider_slave07"), 9300))
  val searchResponse = client.prepareSearch("gnews_raw_data")
    .setQuery(QueryBuilders.rangeQuery("timestamp").gte("2017-11-14T00:00").lt("2017-11-14T10:00"))
    .addAggregation(AggregationBuilders.terms("genre").field("article_genre.keyword")
      .subAggregation(AggregationBuilders.max("max_ts").field("timestamp")))
    .setSize(0)
    .execute.actionGet
  val res = searchResponse.toString
  println(res)
}
