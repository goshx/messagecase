package org.lmars.dm.message;

import io.vertx.core.json.JsonObject;
import org.lmars.dm.conf.Config;


public class GetProjectCatalogResponse extends OkResponse2 {
    
    public GetProjectCatalogResponse set(Project[] projects) {
        put(Config.key_projects, projects);
        return this;
    }

    public static class Topic extends JsonObject{

        static Topic obj = new Topic();

        public static JsonObject set(String name
                , String[] subscribed_users
                , String[] publish_permissions
                , String[] subscribe_permissions) {
            return obj.put(Config.key_name, name)
                    .put(Config.key_subscribed_users, subscribed_users)
                    .put(Config.key_publish_permissions, publish_permissions)
                    .put(Config.key_subscribe_permissions, subscribe_permissions);
        }
    }
    
    public static class Project extends JsonObject{

        static Project obj = new Project();

        public static JsonObject set(String name
                , String description
                , String leaders
                , String[] users
                , String[] hosts
                , Topic[] topics) {
            return obj.put(Config.key_name, name)
                    .put(Config.key_description, description)
                    .put(Config.key_leaders, leaders)
                    .put(Config.key_users, users)
                    .put(Config.key_hosts, hosts)
                    .put(Config.key_topics, topics);
        }
    }
    
}
