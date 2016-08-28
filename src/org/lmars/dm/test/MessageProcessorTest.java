//package org.lmars.dm.test;
//
//import java.util.concurrent.ExecutionException;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.lmars.dm.conf.Config;
//import org.lmars.dm.kafka.MessageProcessor;
//import org.lmars.dm.storage.BaseDriver.Field;
//import org.lmars.dm.storage.BaseDriver.FieldType;
//import org.lmars.dm.storage.PhoenixDriver;
//
//import io.vertx.core.json.JsonObject;
//
//@SuppressWarnings("unused")
//public class MessageProcessorTest {
//	MessageProcessor messageProcessor ;
//	
//	@Before
//	public void before(){
//		
////		Field timeUUID = new Field(FieldType.VARCHAR, "timeUUID", true, false);
////		Field groupid = new Field(FieldType.VARCHAR, "groupid", false, true);
////		Field topic = new Field(FieldType.VARCHAR, "topic", false, true);
////		Field partition = new Field(FieldType.INTEGER, "partition", false, true);
////		Field offset = new Field(FieldType.INTEGER, "offset", false, false);
////		PhoenixDriver.helper().createTable(Config.kafka_offset_table, timeUUID, groupid, topic, partition, offset);
//		
//		messageProcessor = new MessageProcessor();
//	}
//
//	
//	@Test
//	public void testProducer() throws InterruptedException{
//		messageProcessor.produce(Config.topic, "test message" );
//	}
//	
//	
//	@Test
//	public void testConsumer() throws InterruptedException {
//		try {
//			String receivedData = messageProcessor.consume(Config.topic, "group1");
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//	}
//}
