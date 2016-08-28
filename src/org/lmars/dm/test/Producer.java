package org.lmars.dm.test;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.lmars.dm.conf.Config;


public class Producer {
	private final KafkaProducer<Integer, String> producer;

    public Producer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.bootstrap_servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Config.key_serializer_class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Config.value_serializer_class);
        producer = new KafkaProducer<Integer, String>(props);
    }
    
    
    public KafkaProducer<Integer, String> getProducer() {
		return producer;
	}
}
