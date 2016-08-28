package org.lmars.dm.test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.lmars.dm.conf.Config;

import io.vertx.core.json.JsonObject;


public class StreamHandlerTest {

    private String host;
    private int port;
    private Socket socket ;
    
    @Before
    public void beforeTest(){
    	host = "127.0.0.1";
    	port = 7082;
    }

	@Test
	public void testLoginSendSubscribe(){

	    String sessionID = "";
		String topic = "test";

		//test login
		JsonObject login_msg = new JsonObject();
		login_msg.put(Config.user_authority_username, "lican");
		login_msg.put(Config.user_authority_password, "123");
		byte[] login_header = byteMerger(int2byte(0), int2byte(0), int2byte(0), int2byte(0));
		
		try {
			socket = new Socket(host, port);
			String resultString = send(byteMerger(login_header, login_msg.toString().getBytes()));
			JsonObject result = new JsonObject(resultString);
			sessionID = result.getString("SessionID");
			Logger.getLogger(StreamHandlerTest.class.getName()).log(Level.INFO, resultString);
		} catch (IOException e) {
			Logger.getLogger(StreamHandlerTest.class.getName()).log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				if (socket!= null) {
					socket.close();
				}
			} catch (IOException e) {
				Logger.getLogger(StreamHandlerTest.class.getName()).log(Level.SEVERE, e.getMessage());
			}
		}
			
		if (sessionID.length() == 0) {
			return;
		}
		
		int sessionID_length = sessionID.length();
		int topic_length = topic.length();
		
		//test send message
		JsonObject send_msg = new JsonObject();
		send_msg.put("message_value", "send message");
		byte[] send_header = byteMerger(int2byte(1), int2byte(sessionID_length), sessionID.getBytes(), int2byte(0), int2byte(topic_length), topic.getBytes());
		try {
			socket = new Socket(host, port);
			String resultString = send(byteMerger(send_header, send_msg.toString().getBytes()));
			Logger.getLogger(StreamHandlerTest.class.getName()).log(Level.INFO, resultString);
		} catch (IOException e) {
			Logger.getLogger(StreamHandlerTest.class.getName()).log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				if (socket!= null) {
					socket.close();
				}
			} catch (IOException e) {
				Logger.getLogger(StreamHandlerTest.class.getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		
		//test subscribe message
		JsonObject subscribe_msg = new JsonObject();
		subscribe_msg.put(Config.kafka_key_groupid, Config.group_id);
		byte[] subscribe_header = byteMerger(int2byte(2), int2byte(sessionID_length), sessionID.getBytes(), int2byte(0), int2byte(topic_length), topic.getBytes());
		try {
			socket = new Socket(host, port);
			String resultString = send(byteMerger(subscribe_header, subscribe_msg.toString().getBytes()));
			Logger.getLogger(StreamHandlerTest.class.getName()).log(Level.INFO, resultString);
			
		} catch (IOException e) {
			Logger.getLogger(StreamHandlerTest.class.getName()).log(Level.SEVERE, e.getMessage());
		} finally {
			try {
				if (socket!= null) {
					socket.close();
				}
			} catch (IOException e) {
				Logger.getLogger(StreamHandlerTest.class.getName()).log(Level.SEVERE, e.getMessage());
			}
		}
	}
    
	private String send(byte[] bytes) throws IOException {
		
        try (DataOutputStream dos // 建立输出流
                = new DataOutputStream(socket.getOutputStream())) {
            dos.write(bytes, 0, bytes.length); // 向输出流写入 bytes
            
            InputStream inputStream = socket.getInputStream();
            
            byte[] length = new byte[4];
            inputStream.read(length);
            
            byte[] buffer = new byte[byte2int(length, 0)];
            inputStream.read(buffer);

    		String result = new String(buffer, "utf-8");
    		return result;
    		
        } 
    }
	
	public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];

        targets[3] = (byte) (res & 0xff);// 最低位   
        targets[2] = (byte) ((res >> 8) & 0xff);// 次低位   
        targets[1] = (byte) ((res >> 16) & 0xff);// 次高位   
        targets[0] = (byte) (res >>> 24);// 最高位,无符号右移。   
        return targets;
    }
    
    public static int byte2int(byte[] res, int offset) {
        int value = 0;
        
        for (int i = 0; i < 4; i++) {
        	int shift = (4 - 1 - i)*8;
        	value += (res[i+offset] & 0xff) << shift ;
		}
        return value;
    }
	
	public static byte[] byteMerger(byte[]... bytes) {
		int num = 0;
		int tag = 0;
		for (byte[] bs : bytes) {
			num += bs.length;
		}
		byte[] byte_ = new byte[num];
		for (byte[] bs : bytes) {
			System.arraycopy(bs, 0, byte_, tag, bs.length);
			tag += bs.length;
		}
        return byte_;
    }
}
