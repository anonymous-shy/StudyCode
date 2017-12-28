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
//        props.put("partitioner.class", "xyz.shy.Producer.SimplePartitioner");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<>(config);
        String[] guitars = {"Gibson", "Fender", "MusicMan", "ESP", "Ibanez"};
        String[] logos = {"Nike", "Air Jordan", "Supreme", "Converse", "Adidas"};
        String[] stars = {"Shy", "Dilraba", "Emma", "Taylor", "AnonYmous"};
        for (long nEvents = 1; nEvents <= 3600; nEvents++) {
            String ip = "192.168.1." + rnd.nextInt(255);
            String msg = nEvents + "::" + LocalDateTime.now().toString().substring(0, 19);// + "::" + ip;
            int i = rnd.nextInt(stars.length - 1);
            String word = stars[i] + "::" + logos[i] + "::" + guitars[i];
            System.out.println(nEvents + " <-> " + word);
            KeyedMessage<String, String> data = new KeyedMessage<>("topic3", Long.toString(nEvents), word);
            producer.send(data);
            TimeUnit.SECONDS.sleep(1);
        }
        producer.close();
    }
}
