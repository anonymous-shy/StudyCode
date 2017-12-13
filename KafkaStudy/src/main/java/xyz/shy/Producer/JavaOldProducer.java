package xyz.shy.Producer;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shy on 2017/11/28
 */

public class JavaOldProducer {
    public static void main(String[] args) throws InterruptedException {
        Random rnd = new Random();

        Properties props = new Properties();
        props.put("metadata.broker.list", "192.168.71.62:9092,192.168.71.63:9092,192.168.71.64:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "xyz.shy.Producer.SimplePartitioner");
        props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);

        Producer<String, String> producer = new Producer<>(config);

        for (long nEvents = 0; nEvents < 100; nEvents++) {
            String ip = "192.168.2." + rnd.nextInt(255);
            String msg = LocalDateTime.now().toString() + ",www.shy.xyz," + ip;
            KeyedMessage<String, String> data = new KeyedMessage<>("test-topic", ip, msg);
            producer.send(data);
            TimeUnit.SECONDS.sleep(5);
        }
        producer.close();
    }
}
