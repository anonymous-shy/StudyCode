package xyz.shy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Shy on 2018/2/7
 */

public class EsRestApiBatch {

    public static void main(String[] args) throws InterruptedException {

        RestClient restClient = RestClient.builder(
                new HttpHost("tagtic-slave01", 9200, "http"),
                new HttpHost("tagtic-slave02", 9200, "http"),
                new HttpHost("tagtic-slave03", 9200, "http")).build();

        int numRequests = 10;
        final CountDownLatch latch = new CountDownLatch(numRequests);

        HttpEntity[] entities = new HttpEntity[10];

        for (int i = 0; i < numRequests; i++) {
            restClient.performRequestAsync(
                    "PUT",
                    "/twitter/tweet/" + i,
                    Collections.<String, String>emptyMap(),
                    //assume that the documents are stored in an entities array
                    entities[i],
                    new ResponseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            System.out.println(response);
                            latch.countDown();
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            latch.countDown();
                        }
                    }
            );
        }

//wait for all requests to be completed
        latch.await();
    }
}
