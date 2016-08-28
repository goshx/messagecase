package org.lmars.dm.main.agent;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


public class MessageForwarder {
	    
	private KafkaProducer<Integer, String> producer = null;
	private MessageBufferDB  bufDB = new MessageBufferDB();
		
	private static int Max_Sample_Num = 1000;
	private static int Reserve_Size = 10000;
	private StreamQueue queue1 = new StreamQueue(Reserve_Size);
	private StreamQueue queue2 = new StreamQueue(Reserve_Size);
	private Semaphore msgSem = new Semaphore(0);
	private StreamQueue buzyQueue = null;
	private StreamQueue freeQueue = null;
	private Lock qLocker = new ReentrantLock();
	private ExecutorService es = null;
	
	
	public void startUp(String kafkaServers) throws Exception{
		
		Properties props = new Properties();
		props.put("bootstrap.servers", kafkaServers);
		/*props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);*/
		props.put("timeout.ms",3000);
		props.put("max.block.ms",3000);
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		producer = new KafkaProducer<Integer, String>(props);
		
		bufDB.startUp(props);
		buzyQueue = queue1;
		freeQueue = queue2;
		
		 es = Executors.newFixedThreadPool(1);
	        es.submit(()->{
	        	while(true){
					try{
						msgSem.acquire();
						qLocker.lock();	
						//将buzyQueue换下来，换成freeQueue
						if(buzyQueue.equals(queue1)){
							buzyQueue = queue2;
							freeQueue = queue1;
						}else{
							buzyQueue = queue1;
							freeQueue = queue2;						
						}
						buzyQueue.removeAll();					
					}catch(Exception ignore){
						break;
					}finally{
						qLocker.unlock();
					}
					
					//将数据发送到kafka里
					forwardDBData(freeQueue);
				}
	        });       
		
	}
	
	public void processMessage(String topic,String content){
		
		//处理stream消息
		qLocker.lock();
		try{
			buzyQueue.add(new StreamMessage(topic,content));
			if(msgSem.availablePermits() ==0)
				msgSem.release();	
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			qLocker.unlock();
		}
		
	}
	

	public void forwardDBData(StreamQueue freeQueue){
				
		try{
			int sampleN = freeQueue.computeSampleN(Max_Sample_Num);
			if(sampleN ==0)
				return;
			AtomicBoolean flag = new AtomicBoolean(true);
			AtomicInteger msgN = new AtomicInteger(sampleN);
			Semaphore sem = new Semaphore(0);			
			freeQueue.sample(Max_Sample_Num, msg->{
				try{
					if(!flag.get()){
						//错误处理，放到messagebufdb里面
						bufDB.addMessage(msg.topic, msg.content);							
						if(0 == msgN.decrementAndGet())
							sem.release();
					}else{
						//System.out.println("have send:" + msg.content);	
						if(0 == msgN.decrementAndGet())
							sem.release();
						
						producer.send(new ProducerRecord<Integer,String>(msg.topic,1,msg.content), (metadata, e)-> {
							if(e != null){						
								//错误处理，放到messagebufdb里面
								bufDB.addMessage(msg.topic, msg.content);
								flag.set(false);
							}
							//System.out.println("have send:" + msg.content);	
							if(0 == msgN.decrementAndGet())
								sem.release();
						});
					}					
				}catch(Exception ignore){
					ignore.printStackTrace();
				}
			});
			
			sem.acquire();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}
	
}
