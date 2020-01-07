package xyz.shy.Producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import xyz.shy.utils.KafkaCommonUtils;

import java.util.Properties;

public class CallBackProducer {

	public static void main(String[] args) {

		Properties properties = KafkaCommonUtils.ProducerProps();
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		for (int i = 0; i < 100; i++) {
			kafkaProducer.send(new ProducerRecord<>("TOPIC-1", "TEST" + i), (Callback) (recordMetadata, e) -> {
				if (e != null) {
					e.printStackTrace();
				} else
//					System.out.println(recordMetadata.partition() + "-" + recordMetadata.offset());
					System.out.println("record Metadata: " + recordMetadata.toString());
			});
		}

		kafkaProducer.close();
	}
}
