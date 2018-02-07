package xyz.shy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;
import java.util.Collections;

/**
 * Created by Shy on 2018/2/7
 */

public class EsRestApiMultiThread {

    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(
                new HttpHost("tagtic-slave01", 9200, "http"),
                new HttpHost("tagtic-slave02", 9200, "http"),
                new HttpHost("tagtic-slave03", 9200, "http"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultIOReactorConfig(
                                IOReactorConfig.custom().setIoThreadCount(1).build());
                    }
                })
                .build();
        HttpEntity entity = new NStringEntity(
                "{\n" +
                        "    \"user\" : \"kimchy\",\n" +
                        "    \"post_date\" : \"2009-11-15T14:12:12\",\n" +
                        "    \"message\" : \"trying out Elasticsearch\"\n" +
                        "}", ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(
                "GET",
                "/twitter/_search",
                Collections.<String, String>emptyMap());
        System.out.println(response);
    }
}
