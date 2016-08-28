package org.lmars.dm.message;

import io.vertx.core.json.JsonObject;
import org.lmars.dm.conf.Config;


public class GetTopicCatalogResponse extends OkResponse2 {
    
    public GetTopicCatalogResponse set(Topic[] topics) {
        put(Config.key_topics, topics);
        return this;
    }
    
    public static class Topic extends JsonObject{

        static Topic obj = new Topic();

        public static JsonObject set(String name
                , String[] hosts
                , String[] subscribed_users
                , String[] publish_permissions
                , String[] subscribe_permissions) {
            return obj.put(Config.key_name, name)
                    .put(Config.key_hosts, hosts)
                    .put(Config.key_subscribed_users, subscribed_users)
                    .put(Config.key_publish_permissions, publish_permissions)
                    .put(Config.key_subscribe_permissions, subscribe_permissions);
        }
    }
    
}
