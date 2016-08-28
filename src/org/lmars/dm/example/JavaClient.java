package org.lmars.dm.example;

import io.vertx.core.json.JsonObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lmars.dm.conf.Config;
import org.lmars.dm.message.LoginRequest;
import org.lmars.dm.message.LoginResponse;
import org.lmars.dm.message.StatusResponse;
import static org.lmars.dm.storage.StorgeService.cfgFile;
import org.lmars.dm.util.ConfigureFileParser;

public class JavaClient {
	
	public Socket socket;
	public String sessionID;
        
        public static String cfgFile;
        public static String serverHost;
        public static String msgTopic;
        public static int msgThread;
        public static int serverPort;
        public static int msgNumPerThread;
        public static String username;
        public static String password;
        
        
        public static void readConfig() throws Exception{
		//加载配置信息
		ConfigureFileParser cfg = new ConfigureFileParser();
		Map<String,Map<String,String>> sections = cfg.parse2(cfgFile);
                
		//kafka
		Map<String,String> client_conf = sections.get("client");
                serverHost = client_conf.get("host");
                serverPort = Integer.valueOf(client_conf.get("port"));
                username = client_conf.get("username");
                password = client_conf.get("password");
                msgTopic = client_conf.get("topic");
                msgThread = Integer.valueOf(client_conf.get("thread"));
                msgNumPerThread = Integer.valueOf(client_conf.get("msg_number_per_thread"));
	}

    public static void main(String[] args) throws Exception {
        
        if(args.length>0) {
            cfgFile = args[0];
        } else {
            cfgFile = "C:\\Users\\Junia\\Documents\\NetBeansProjects\\DMessage\\conf\\client.cfg";
        }
        
        readConfig();
    	
    	String[] socket_address = {serverHost};
    	int thread_n = msgThread;
	    String topic_c = msgTopic;
	    int message_n_c = msgNumPerThread;
    	if(args.length != 0){
    		thread_n = Integer.parseInt(args[0]);
    		message_n_c = Integer.parseInt(args[1]);
    	}
    	
    	final String topic = topic_c;
    	final int message_n = message_n_c;
	    int port = serverPort;
	    double baseLongitude = 114.30;
	    double baseLatitude = 30.57;
		for (int i = 0; i < thread_n; i++) {
			ExecutorService execution = Executors.newSingleThreadExecutor();
			execution.submit(() -> {
				try{
					String host = socket_address[new Random().nextInt(socket_address.length )];
					JavaClient jc = new JavaClient();
					jc.login(host, port, username, password);
					
					Random random = new Random();
					for(int n=0;n<message_n;n++){
						double floatLongitude = random.nextDouble() * 6 - 3;
						double floatLatitude = random.nextDouble() * 4 - 2;
						JsonObject send_msg = new JsonObject();
						send_msg.put("device", Thread.currentThread().getName());
						send_msg.put("time",System.currentTimeMillis());
						send_msg.put("lat",baseLatitude + floatLatitude);
						send_msg.put("lon",baseLongitude + floatLongitude);
						int precision = 5 * (random.nextInt(9) + 1);
						send_msg.put("precision", precision);
						jc.sendMessage(topic_c, send_msg.toString());
						Thread.sleep(100);
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			});
		}
    }
    
	public void login(String host, int port, String user,String password) throws Exception {
				
		// login
		LoginRequest login_req = new LoginRequest();
		login_req.userName = user;
		login_req.userPassword = password;
		String content = login_req.toString();
		int package_size = 4 + 4 + 0 +  4 + content.length();
		socket = new Socket(host, port);
		String resultString = send(byteMerger(int2byte(package_size), int2byte(Config.Message_Login), int2byte(0), int2byte(content.length()), content.getBytes()));;		
		LoginResponse r = new LoginResponse();
		r.fromJson(new JsonObject(resultString));
		if(!r.status)
			throw new Exception();
		this.sessionID = r.sessionID;
	}
    
    public void sendMessage(String topic ,String content) throws Exception{
    	
    	int package_size = 4 + 4 + topic.length() + 4 + content.length();		
		String resultString = send(byteMerger(int2byte(package_size),int2byte(Config.Message_Send),int2byte(topic.length()), topic.getBytes(), int2byte(content.length()),content.getBytes()));
		StatusResponse r = new StatusResponse().fromJson(new JsonObject(resultString));
		if(!r.status)
			throw new Exception();
		
    }
    
    private String send(byte[] bytes) throws IOException {
    	
    	DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
    	dos.write(bytes, 0, bytes.length); // 向输出流写入 bytes

        InputStream inputStream = socket.getInputStream();

        byte[] length = new byte[4];
        inputStream.read(length);

        byte[] buffer = new byte[byte2int(length, 0)];
        inputStream.read(buffer);

        String result = new String(buffer, "utf-8");
        return result;
    }

    private static byte[] int2byte(int res) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (res & 0xff);// 最低位   
        targets[2] = (byte) ((res >> 8) & 0xff);// 次低位   
        targets[1] = (byte) ((res >> 16) & 0xff);// 次高位   
        targets[0] = (byte) (res >>> 24);// 最高位,无符号右移。   
        return targets;
    }

    private static int byte2int(byte[] res, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (res[i + offset] & 0xff) << shift;
        }
        return value;
    }

    private static byte[] byteMerger(byte[]... bytes) {
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
