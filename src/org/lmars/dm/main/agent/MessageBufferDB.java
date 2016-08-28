package org.lmars.dm.main.agent;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.h2.jdbcx.JdbcConnectionPool;


public class MessageBufferDB {
	
	private JdbcConnectionPool cp = null;
	private KafkaProducer<Integer, String> producer = null;
	private ExecutorService es = null;
	private Properties kafkaProps = null;
	static final private Integer OnTimeMessageN = new Integer(1000);
	static final private Integer SleepTime = 1000;
	
	public MessageBufferDB(){
		
	}
	
	public static boolean createDir(String destDirName) {  
        File dir = new File(destDirName);  
        if (dir.exists()) {  
            return true;  
        }  
        if (!destDirName.endsWith(File.separator)) {  
            destDirName = destDirName + File.separator;  
        }  
        //创建目录  
        if (dir.mkdirs()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
	
	
	public void startUp(Properties kafakProps) throws Exception{
		
		this.kafkaProps = kafakProps;
		producer = new KafkaProducer<Integer, String>(kafkaProps);
		
		String path = System.getProperty("user.dir") + "\\MsgBufDB";
		if(!createDir(path))
			throw new Exception();
		
		String db_url = "jdbc:h2:" + path + "\\bufdb" ;

        Class.forName("org.h2.Driver");
        cp = JdbcConnectionPool.create(db_url, "sa", "sa");
        createBufferTable();
        		
        es = Executors.newFixedThreadPool(1);
        es.submit(()->{
        	resendBufferMessages();
        });        
	}
	
	private void createBufferTable() throws Exception{
		try(Connection conn = cp.getConnection();){
			String sql = "CREATE TABLE IF NOT EXISTS sys_msg_buffer_table(id int IDENTITY PRIMARY KEY, content VARCHAR(5000), topic VARCHAR(100));";
			conn.createStatement().execute(sql);
		}catch(Exception e){
			throw e;
		}
	}
	
	public void addMessage(String topic,String content){
		try(Connection conn = cp.getConnection();
				Statement stat = conn.createStatement();)
			{
				String sql = "insert into sys_msg_buffer_table(content,topic) values('" + content + "','" + topic +"')";
				stat.execute(sql);							
			}catch(Exception e){
				e.printStackTrace();
			}
	}
		
	public class BufferMsg{
		
		public BufferMsg(int id_,String topic_,String content_){
			this.id = id_;
			this.topic = topic_;
			this.content = content_;
		}
		public int id;
		public String topic;
		public String content;
	}
	
	private void resendBufferMessages(){
		
		while(true){
			try{			
				List<BufferMsg> jmsgs = new ArrayList<BufferMsg>();
				try(Connection conn = cp.getConnection();
					Statement stat = conn.createStatement();
					ResultSet rs =  stat.executeQuery("select top " + OnTimeMessageN + " * from  sys_msg_buffer_table order by id DESC");)
				{
					//一次从数据库中读取数据100条
					while (rs.next()) {
						int id = rs.getInt("id");
						String content_str = rs.getString("content");
						String topic_str = rs.getString("topic");
						jmsgs.add(new BufferMsg(id,topic_str,content_str));
					}
				}catch(Exception e){
					e.printStackTrace();
					break;
				}
				
				if(jmsgs.isEmpty()){
					Thread.sleep(SleepTime);
					continue;
				}
					
				//发送数据
				AtomicInteger msgN = new AtomicInteger(jmsgs.size());
				Semaphore sem = new Semaphore(0);
				AtomicBoolean flag = new AtomicBoolean(true);
				
				for(BufferMsg jmsg : jmsgs){
					final Integer id = jmsg.id;
					if(!flag.get()){
						if(msgN.decrementAndGet() == 0)
							sem.release();
					}else{
						producer.send(new ProducerRecord<Integer,String>(jmsg.topic,1,jmsg.content), (metadata, e)-> {
							if(e == null){					
								//发送成功，将其从数据库中删除
								try(Connection conn = cp.getConnection();
									Statement stat = conn.createStatement();){
									stat.execute("delete from sys_msg_buffer_table where id =" + id);
								}catch(Exception ignore){
									ignore.printStackTrace();
								}						
							}else{
								flag.set(false);
							}
							
							if(msgN.decrementAndGet() == 0)
								sem.release();
						});							
					}

				}
				
				try{
					sem.acquire();
					if(!flag.get()){
						Thread.sleep(SleepTime);
					}
				}catch(Exception e){
					break;
				}
				
			}catch(Exception ignore){
				
			}
		}
		
	}
	
}
