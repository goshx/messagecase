package org.lmars.dm.test;

import io.vertx.core.json.JsonArray;

import org.lmars.dm.conf.Config;
import org.lmars.dm.message.OkResponse2;

public class SubscribeResponse extends OkResponse2 {
    
//    public SubscribeResponse set(int partition, int offset, JsonObject contents) {
//        put(Config.kafka_key_partition, partition)
//        	.put(Config.key_offset, offset)
//                .put(Config.key_contents, contents);
//        return this;
//    }
	
	public SubscribeResponse set(JsonArray partitionOffsets, JsonArray contents) {
		put(Config.kafka_key_partitionOffsetPair, partitionOffsets);
        put(Config.key_contents, contents);
        return this;
    }
    
}
