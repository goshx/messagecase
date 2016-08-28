package org.lmars.dm.tcp;

import org.lmars.dm.conf.Config;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

public class TcpRequest extends JsonObject {

    private int 		type;
    private String 		topic;
    private String 		content;
    
    public TcpRequest(){
    	
    }
    
    public TcpRequest(Buffer msg,int offset, int len)  throws Exception {
    	fromBuffer(msg,offset,len);
    }
    
    public void fromBuffer(Buffer msg,int offset, int len) throws Exception {
	   type = msg.getInt(offset);
	   offset += 4;
	   int topic_len = msg.getInt(offset);
	   offset += 4;
	   topic = msg.getString(offset, offset + topic_len);
	   offset += topic_len;
	   int content_len = msg.getInt(offset);
	   if((4 + 4 + topic_len + 4 + content_len) != len)
		   throw new Exception();
	   offset += 4;
	   content = msg.getString(offset, offset + content_len);
    }
    
    public void toBuffer(Buffer msg){
    	msg.appendInt(type);
    	msg.appendInt(topic.length());
    	msg.appendString(topic);
    	msg.appendInt(content.length());
    	msg.appendString(content);
    	
    }
    
    public int getMessageType() {
        return type;
    }

    public String getTopic() {
        return topic;
    }

    public String getContent() {
        return content;
    }
 
    
}
