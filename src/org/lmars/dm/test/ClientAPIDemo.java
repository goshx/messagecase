package org.lmars.dm.test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.junit.Test;
import org.lmars.dm.util.RandomSessionID;

import io.vertx.core.json.JsonObject;

public class ClientAPIDemo {
	
	private int messageType ;//login = 0, send = 1, subscribe = 2
	private int sessionid_length ;
	private int project_length ;
	private int topic_length ;
	private String sessionid ;
	private String project ;
	private String topic ;
	private String content ;
	
	@Test
	public void login(){
		messageType = 0;
		sessionid = RandomSessionID.getUUID();
		sessionid_length = sessionid.length();
		project_length = 0;
		topic_length = 0;
		JsonObject msg = new JsonObject();
		msg.put("username", "john");
		msg.put("password", "123");
		content = msg.toString();
		byte[] header = byteMerger(int2byte(messageType), int2byte(sessionid_length), sessionid.getBytes(), int2byte(project_length), int2byte(topic_length));
		try {
			send(byteMerger(header, content.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void sendMessage(){
		messageType = 1;
		sessionid = RandomSessionID.getUUID();
		sessionid_length = sessionid.length();
		project = "project_01";
		project_length = project.length();
		topic = "topic_02";
		topic_length = topic.length();
		JsonObject msg = new JsonObject();
		msg.put("key1", "value1");
		msg.put("key2", "value2");
		content = msg.toString();
		byte[] header = byteMerger(int2byte(messageType), int2byte(sessionid_length), sessionid.getBytes(), int2byte(project_length), project.getBytes(), int2byte(topic_length), topic.getBytes());
		try {
			send(byteMerger(header, content.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void subscribeMessage(){
		messageType = 2;
		sessionid = RandomSessionID.getUUID();
		sessionid_length = sessionid.length();
		project = "project_01";
		project_length = project.length();
		topic = "topic_01";
		topic_length = topic.length();
		JsonObject msg = new JsonObject();
		msg.put("groupid", "group1");
		content = msg.toString();
		byte[] header = byteMerger(int2byte(messageType), int2byte(sessionid_length), sessionid.getBytes(), int2byte(project_length), project.getBytes(), int2byte(topic_length), topic.getBytes());
		try {
			send(byteMerger(header, content.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private byte[] int2byte(int res) {
        byte[] targets = new byte[4];

        targets[3] = (byte) (res & 0xff);// 最低位   
        targets[2] = (byte) ((res >> 8) & 0xff);// 次低位   
        targets[1] = (byte) ((res >> 16) & 0xff);// 次高位   
        targets[0] = (byte) (res >>> 24);// 最高位,无符号右移。   
        return targets;
    }
	
	private byte[] byteMerger(byte[]... bytes) {
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
	
	private void send(byte[] bytes) throws IOException {
    	
        Socket socket = new Socket("127.0.0.1", 7082); // 建立和服务端的 socket
        
        try (DataOutputStream dos // 建立输出流
                = new DataOutputStream(socket.getOutputStream())) {
            dos.write(bytes, 0, bytes.length); // 向输出流写入 bytes
        } finally {
            socket.close();
		}
    }
}
