package shy.Consumer;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Shy on 2017/11/28
 * 设计一个高级消费者（Designing a High Level Consumer）
 * 了解使用高层次消费者的第一件事是，它可以（而且应该！）是一个多线程的应用。线程围绕在你的主题分区的数量，有一些非常具体的规则：
 * 如果你提供比在topic分区多的线程数量，一些线程将永远不会看到消息。
 * 如果你提供的分区比你拥有的线程多，线程将从多个分区接收数据。
 * 如果你每个线程上有多个分区，对于你以何种顺序收到消息是没有保证的。
 * 举个例子，你可能从分区10上获取5条消息和分区11上的6条消息，然后你可能一直从10上获取消息，即使11上也拥有数据。
 * 添加更多的进程线程将使kafka重新平衡，可能改变一个分区到线程的分配。
 * TODO shit!!! 1,多线程 Runnable 2, Executors
 */

public class JavaOldHighConsumer {
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;

    public JavaOldHighConsumer(String a_zookeeper, String a_groupId, String a_topic) {

        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(a_zookeeper, a_groupId));
        this.topic = a_topic;
    }

    public void shutdown() {
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
    }

    public void run(int a_numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, a_numThreads);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // now launch all the threads
        //
        executor = Executors.newFixedThreadPool(a_numThreads);

        // now create an object to consume the messages
        //
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new ConsumerRun(stream, threadNumber));
            threadNumber++;
        }
    }

    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");

        return new ConsumerConfig(props);
    }

    public static void main(String[] args) {
        String[] arg = {"tagtic-master:2181", "group-shy", "test-topic", "3"};
        args = arg;
        String zooKeeper = args[0];
        String groupId = args[1];
        String topic = args[2];
        int threads = Integer.parseInt(args[3]);

        JavaOldHighConsumer consumer = new JavaOldHighConsumer(zooKeeper, groupId, topic);
        consumer.run(threads);

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ie) {
//            ie.printStackTrace();
//        }
//        consumer.shutdown();
    }
}
