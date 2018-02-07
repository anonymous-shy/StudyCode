package xyz.shy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

/**
 * Created by Shy on 2018/2/6
 */

public class EsRestApi {

    public static void main(String[] args) throws IOException {
        RestClient restClient = RestClient.builder(
                new HttpHost("tagtic-slave01", 9200, "http"),
                new HttpHost("tagtic-slave02", 9200, "http"),
                new HttpHost("tagtic-slave03", 9200, "http")).build();

        Response response = restClient.performRequest("GET", "/", Collections.singletonMap("pretty", "true"));
        System.out.println(EntityUtils.toString(response.getEntity()));
        System.out.println("****************************************");
        System.out.println(searchDemo(restClient));
        //index a document
        /*HttpEntity entity = new NStringEntity(
                "{\n" +
                        "    \"user\" : \"kimchy\",\n" +
                        "    \"post_date\" : \"2009-11-15T14:12:12\",\n" +
                        "    \"message\" : \"trying out Elasticsearch\"\n" +
                        "}", ContentType.APPLICATION_JSON);
        Response indexResponse = restClient.performRequest(
                "PUT",
                "/twitter/tweet/1",
                Collections.<String, String>emptyMap(),
                entity);*/
        restClient.close();
    }

    static String searchDemo(RestClient restClient) throws IOException {
        HttpEntity entity = new NStringEntity(
                "{\n" +
                        "  \"query\": {\n" +
                        "    \"term\": {\n" +
                        "      \"gender.keyword\": {\n" +
                        "        \"value\": \"F\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}", ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(
                "GET",
                "/bank/account/_search",
                Collections.singletonMap("pretty", "true"),
                entity);

        return EntityUtils.toString(response.getEntity());
    }
}
