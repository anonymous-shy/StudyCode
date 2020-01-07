package xyz.shy.Consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class MyConsumer {

	public static void main(String[] args) {

		// 1.消费者配置信息
		Properties properties = new Properties();
		// 2.配置信息赋值
		// 连接的集群
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "");
		// 开启自动提交
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		// 自动提交延迟
		properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		// K，V的反序列化
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "");
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "");
		// 消费者组
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "");
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);

		// 订阅主题
		kafkaConsumer.subscribe(Collections.singletonList("Topic-1"));
	}
}
