package xyz.shy.utils;

import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class KafkaCommonUtils {

	public static Properties ProducerProps() {
		Properties properties = new Properties();
		// Kafka 集群
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.71.62:19092,192.168.71.63:19092,192.168.71.64:19092");
		// ACK应答级别
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		// 重试次数
		properties.put(ProducerConfig.RETRIES_CONFIG, 0); // 消息发送请求失败重试次数
		// 批次大小
		properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 2000);
		// 等待时间
		properties.put(ProducerConfig.LINGER_MS_CONFIG, 1); // 消息逗留在缓冲区的时间，等待更多的消息进入缓冲区一起发送，减少请求发送次数
		// RecordAccumulator缓冲区大小
		properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 内存缓冲区的总量
		// 如果发送到不同分区，并且不想采用默认的Utils.abs(key.hashCode) % numPartitions分区方式，则需要自己自定义分区逻辑
		 properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "xyz.shy.Producer.SimplePartitioner");
		// K，V的序列化类
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		return properties;
	}
}
