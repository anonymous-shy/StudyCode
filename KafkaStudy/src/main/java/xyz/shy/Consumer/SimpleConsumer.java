//package xyz.shy.Consumer;
//
//import kafka.consumer.ConsumerConfig;
//import kafka.consumer.ConsumerIterator;
//import kafka.consumer.KafkaStream;
//import kafka.serializer.StringDecoder;
//import kafka.utils.VerifiableProperties;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
///**
// * Created by Shy on 2018/7/16
// */
//
//public class SimpleConsumer {
//    private static kafka.javaapi.consumer.ConsumerConnector consumer;
//
//    public static void consume() {
//
//        Properties props = new Properties();
//        // zookeeper 配置
//        props.put("zookeeper.connect", "tagtic-master:2181");
//
//        // group 代表一个消费组
//        props.put("group.id", "group-donews");
//
//        // zk连接超时
//        props.put("zookeeper.session.timeout.ms", "4000");
//        props.put("zookeeper.sync.time.ms", "200");
//        props.put("auto.commit.interval.ms", "1000");
//        props.put("auto.offset.reset", "smallest");
//        // 序列化类
//        props.put("serializer.class", "kafka.serializer.StringEncoder");
//
//        ConsumerConfig config = new ConsumerConfig(props);
//
//        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
//
//        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
//        topicCountMap.put("request_new", new Integer(1));
//
//        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
//        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
//
//        Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,
//                keyDecoder, valueDecoder);
//        KafkaStream<String, String> stream = consumerMap.get("request_new").get(0);
//        ConsumerIterator<String, String> it = stream.iterator();
//        while (it.hasNext())
//            System.out.println(it.next().message());
//    }
//
//    public static void main(String[] args) {
//        consume();
//    }
//}
