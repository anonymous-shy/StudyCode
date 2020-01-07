package xyz.shy.Producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class MyPartitioner implements Partitioner {
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		Integer countForTopic = cluster.partitionCountForTopic(topic);
		return key.toString().hashCode() % countForTopic;
	}


	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> map) {

	}
}
