package org.lmars.dm.storage;

import java.util.List;
import java.util.Map;

import org.lmars.dm.main.agent.StreamMessage;

public interface IStorgeDriver {
	
	void init(Map<String,String> conf, Map<String, TopicMapping> topics) throws Exception;
	
	void saveStreamData(List<StreamMessage> msgs) throws Exception;
	
	boolean reConnect();
	
	void close();
}
