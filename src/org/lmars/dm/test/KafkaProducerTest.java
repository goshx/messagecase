//package org.lmars.dm.test;
//
//import java.util.Properties;
//import java.util.concurrent.ExecutionException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import org.apache.kafka.clients.producer.Callback;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.lmars.dm.conf.Config;
//
//
//public class KafkaProducerTest extends Thread{
//	private final KafkaProducer<Integer, String> producer;
//    private final String topic;
//    private final Boolean isAsync;
//
//    public KafkaProducerTest(String topic, Boolean isAsync) {
//        Properties props = new Properties();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.bootstrap_servers);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Config.key_serializer_class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Config.value_serializer_class);
//        producer = new KafkaProducer<Integer, String>(props);
//        this.topic = topic;
//        this.isAsync = isAsync;
////        this.message_key = message_key;
////        this.message_content = message_content;
//    }
//
//    @Override
//	public void run() {
//        long startTime = System.currentTimeMillis();
//        if (isAsync) { // Send asynchronously
//        	try {
//        		producer.send(new ProducerRecord<Integer, String>(topic, 
//                        0,
//                        "new message"), new DemoCallBack(startTime, 0, "new message"));
//        		
//                    System.out.println("Sent message: (" + 0 + ", " + "new message" + ")");
//                    Logger.getLogger(KafkaProducerTest.class.getName()).log(Level.INFO, "Sent message: (" + 0 + ", " + "new message" + ")");
//			} finally {
//	            producer.close();
//			}
//        } else { // Send synchronously
//            try {
//            	//获取发送消息的详细信息
////                RecordMetadata rm = producer.send(new ProducerRecord<Integer, String>(topic,
////                    _key,
////                    content)).get();
//            	producer.send(new ProducerRecord<Integer, String>(topic,
//                        0,
//                        "new message")).get();
//                Logger.getLogger(KafkaProducerTest.class.getName()).log(Level.INFO, "Sent message: (" + 0 + ", " + "new message" + ")");
//            } catch (InterruptedException e) {
//            	Logger.getLogger(KafkaProducerTest.class.getName()).log(Level.SEVERE, e.getMessage());
//            } catch (ExecutionException e) {
//            	Logger.getLogger(KafkaProducerTest.class.getName()).log(Level.SEVERE, e.getMessage());
//            } finally {
//                producer.close();
//			}
//        }
//    }
//}
//
//class DemoCallBack implements Callback {
//
//    private long startTime;
//    private int key;
//    private String message;
//
//    public DemoCallBack(long startTime, int key, String message) {
//        this.startTime = startTime;
//        this.key = key;
//        this.message = message;
//    }
//
//    /**
//     * A callback method the user can implement to provide asynchronous handling of request completion. This method will
//     * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
//     * non-null.
//     *
//     * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
//     *                  occurred.
//     * @param exception The exception thrown during processing of this record. Null if no error occurred.
//     */
//    @Override
//	public void onCompletion(RecordMetadata metadata, Exception exception) {
//        long elapsedTime = System.currentTimeMillis() - startTime;
//        if (metadata != null) {
//        	Logger.getLogger(KafkaProducerTest.class.getName()).log(Level.INFO, "message(" + key + ", " + message + ") sent to partition(" + metadata.partition() +
//                    "), " +
//                    "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
//        } else {
//        	Logger.getLogger(KafkaProducerTest.class.getName()).log(Level.SEVERE, exception.getMessage());
//        }
//    }
//}
