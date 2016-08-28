package org.lmars.dm.test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class TopicConsumerPool {
	
	/*
	public KafkaConsumer<Integer, String> consumer = null;
	public Map<String,List<Socket>> back_topic_sockets = new ConcurrentHashMap<String,List<Socket>>();
	public Map<String,Set<Socket>> front_topic_sockets = new ConcurrentHashMap<String,Set<Socket>>();
	public boolean topic_is_dirty = false;
	public Set<String> socket_dirty_flags = new HashSet<String>();
	private ExecutorService es = null;
	
	private void clone(Map<String,List<Socket>> src, Map<String,List<Socket>> desk){
		desk.clear();
		desk.putAll(src);
	}
	
	public void startUp(Properties kafakProps){
		
		back_topic_sockets.clear();
		for(Map.Entry<String, Set<Socket>> t_s : front_topic_sockets.entrySet()){
			List<Socket> 
		}
		
		consumer = new KafkaConsumer<Integer, String>(kafakProps);		
        es = Executors.newFixedThreadPool(1);
        es.submit(()->{
        	
        	final int minBatchSize = 200;
        	while(true){
        		try{
        			synchronized(front_topic_sockets){
                		if(topic_is_dirty){
                			List<String> topics = new ArrayList<String>();
                			for(String topic : front_topic_sockets.keySet()){
                				topics.add(topic);
                			}
                			consumer.subscribe(topics);
                		}
                		
                		for(String topic_dirty : socket_dirty_flags){
                			back_topic_sockets.remove(topic_dirty);
                			Set<Socket> sockets = front_topic_sockets.get(topic_dirty);
                			if(sockets != null && !sockets.isEmpty()){
                				List<Socket> ss = new ArrayList<Socket>();
                				ss.addAll(sockets);
                				back_topic_sockets.put(topic_dirty, ss);
                			}
                		}
                		
                		socket_dirty_flags.clear();
                		topic_is_dirty = false;
                	}
        			
        			consumer.seekToEnd(arg0);
                	ConsumerRecords<Integer, String> records = consumer.poll(1000);
                	for (ConsumerRecord<Integer, String> record : records) {
                		String topic = record.topic();
                		List<Socket> ss = back_topic_sockets.get(topic);
                		if(ss != null){
                			for(Socket s : ss){
                				sendFrame(new SubscribeResponse(), s);
                			}
                		}
                		
                    }
                	consumer.commitSync();
        		}catch(Exception e){
        			
        		}
        	}
        	
        });  
        
	}
	
	public void addSession(String topic,Socket socket){
		
		synchronized(front_topic_sockets){
			Set<Socket> sockets = front_topic_sockets.get(topic);
			if(sockets == null){
				sockets = new HashSet<Socket>();
				sockets.add(socket);
				front_topic_sockets.put(topic, sockets);
				topic_is_dirty = true;
				socket_dirty_flags.add(topic);
			}else{
				if(!sockets.contains(socket)){
					sockets.add(socket);
					socket_dirty_flags.add(topic);
				}
			}
		}
		
	}
	
	public void removeSession(Socket socket){
		synchronized(front_topic_sockets){
			List<String> empty_topics = new ArrayList<String>();
			for(Map.Entry<String,Set<Socket>> e : front_topic_sockets.entrySet()){
				if(e.getValue().remove(socket)){
					socket_dirty_flags.add(e.getKey());
				}
				if(e.getValue().isEmpty()){
					empty_topics.add(e.getKey());
				}
			}
			for(String topic : empty_topics){
				topic_is_dirty = true;
				front_topic_sockets.remove(topic);
			}
		}
	}*/
	
	
	
}
