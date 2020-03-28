package xyz.shy.Producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shy on 2017/11/27
 */

public class JavaNewProducer {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        threadPool.execute(new GenTopicProducer("T1"));
        threadPool.execute(new GenTopicProducer("T2"));
    }
}


class GenTopicProducer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(GenTopicProducer.class);

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

    private String topic;

    public GenTopicProducer(String topic) {
        this.topic = topic;
    }

    @Override
    public void run() {
        Properties properties = createProperties();
        try (Producer<String, String> producer = new KafkaProducer<>(properties)) {
            int i, j;
            Random rnd = new Random();
            String[] search_word = {"Gibson", "Fender", "MusicMan", "ESP", "Nike", "AirJordan", "PRS", "Adidas", "Puma"};
            String[] words = {"Hadoop", "Yarn", "MapReduce",
                    "Spark", "SparkCore", "SparkSQL", "SparkStreaming", "SparkML",
                    "Spring", "Kafka", "Mybatis", "Java", "Scala", "Python", "HBase"};
            do {
                TimeUnit.SECONDS.sleep(1);
                String ip = "192.168.1." + rnd.nextInt(255);
                i = rnd.nextInt(search_word.length - 1);
                j = rnd.nextInt(words.length - 1);
                String word = search_word[i] + "::" + words[j];
                String msg = LocalDateTime.now().toString() + "::" + ip + "::" + word;
                System.out.println(msg);
                String key = Integer.toString(i);
                ProducerRecord<String, String> record = new ProducerRecord<>(this.topic, key, msg);
                producer.send(record, (metadata, e) -> {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        System.out.println("Topic: -> " + metadata.topic() +
                                ", Partition: -> " + metadata.partition() +
                                ", Offset: -> " + metadata.offset());
                    }
                });
            } while (true);
        } catch (Exception e) {
            logger.warn("{}", e);
        }
    }
}