package org.lmars.dm.test;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.lmars.dm.conf.Config;


public class Consumer {
	
	private final KafkaConsumer<Integer, String> consumer;

	public Consumer(String groupid, String... topic) {
		Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.bootstrap_servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupid);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Config.key_deserializer_class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Config.value_deserializer_class);

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
	}

	public KafkaConsumer<Integer, String> getConsumer() {
		return consumer;
	}
}
