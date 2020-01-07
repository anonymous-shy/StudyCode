package xyz.shy.Producer;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shy on 2017/11/27
 */

public class JavaNewProducer {
    private static final Logger logger = LoggerFactory.getLogger(JavaNewProducer.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.71.62:9092,192.168.71.63:9092,192.168.71.64:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 2000);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        System.out.println(producer);
        for (int i = 0; i < 100; i++) {
            String value = "[" + i + "] " + LocalDateTime.now().toString();
            System.out.println(value);
            producer.send(new ProducerRecord<>("test-topic", Integer.toString(i), value)).get();
        }
        producer.close();*/
        sendMessage();
    }

    private static Properties createProperties() {
        Properties properties = new Properties();
//        properties.put("bootstrap.servers", "192.168.71.62:9092,192.168.71.63:9092,192.168.71.64:9092");
        properties.put("bootstrap.servers", "192.168.71.62:19092,192.168.71.63:19092,192.168.71.64:19092");
        properties.put("acks", "all");
        properties.put("retries", 0); // 消息发送请求失败重试次数
        properties.put("batch.size", 2000);
        properties.put("linger.ms", 1); // 消息逗留在缓冲区的时间，等待更多的消息进入缓冲区一起发送，减少请求发送次数
        properties.put("buffer.memory", 33554432); // 内存缓冲区的总量
        // 如果发送到不同分区，并且不想采用默认的Utils.abs(key.hashCode) % numPartitions分区方式，则需要自己自定义分区逻辑
        // properties.put("partitioner.class", "xyz.shy.Producer.SimplePartitioner");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

    private static void sendMessage() {
        Properties properties = createProperties();
        Producer<String, String> producer = new KafkaProducer<>(properties);
        int i, j;
        Random rnd = new Random();
        String[] search_word = {"Gibson", "Fender", "MusicMan", "ESP", "Nike", "AirJordan", "PRS", "Adidas", "Puma"};
        String[] words = {"Hadoop", "Yarn", "MapReduce",
                "Spark", "SparkCore", "SparkSQL", "SparkStreaming", "SparkML",
                "Spring", "Kafka", "Mybatis", "Java", "Scala", "Python", "HBase"};
        try {
            while (true) {
                TimeUnit.SECONDS.sleep(1);
                String ip = "192.168.1." + rnd.nextInt(255);
                i = rnd.nextInt(search_word.length - 1);
                j = rnd.nextInt(words.length - 1);
                String word = search_word[i] + "::" + words[j];
                String msg = LocalDateTime.now().toString() + "::" + ip + "::" + word;
                System.out.println(msg);
                String key = Integer.toString(i);
                ProducerRecord<String, String> record = new ProducerRecord<>("T1", key, msg);
                producer.send(record, new Callback() {
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            System.out.println("partition: -> " + metadata.partition() + ", offset: -> " + metadata.offset());
                        }
                    }
                });
            }
        } catch (Exception e) {
            logger.warn("{}", e);
        } finally {
            producer.close();
        }
    }
}
