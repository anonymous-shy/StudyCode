package xyz.shy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * Created by Shy on 2017/11/14
 */

public class EsJavaAPI {
    public static void main(String[] args) throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name", "tagtic-es-cluster")
                .build();
        //Add transport addresses and do something with the client...
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("spider_slave05"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("spider_slave06"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("spider_slave07"), 9300));
        SearchResponse searchResponse = client.prepareSearch("gnews_raw_data")
                .setQuery(QueryBuilders.rangeQuery("timestamp").gte("2017-11-14T00:00").lt("2017-11-14T10:00"))
                .addAggregation(AggregationBuilders.terms("genre").field("article_genre.keyword")
                        .subAggregation(AggregationBuilders.max("max_ts").field("timestamp")))
                .setSize(0)
                .execute().actionGet();
        JSONObject jsonObject = JSON.parseObject(searchResponse.toString());
        JSONObject aggregations = (JSONObject) jsonObject.get("aggregations");
        JSONObject data_source_ids = (JSONObject) aggregations.get("data_source_ids");
        JSONArray buckets = data_source_ids.getJSONArray("buckets");


    }
}
