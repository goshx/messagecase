package org.lmars.dm.message;

import io.vertx.core.json.JsonObject;
import org.lmars.dm.conf.Config;


public class GetUserCatalogResponse extends OkResponse2 {
    
    public GetUserCatalogResponse set(User[] users) {
        put(Config.key_users, users);
        return this;
    }
    
    public static class User extends JsonObject{

        static User obj = new User();

        public static JsonObject set(String name
                , Project[] projects) {
            return obj.put(Config.key_name, name)
                    .put(Config.key_projects, projects);
        }
    }
    
    public static class Project extends JsonObject{

        static Project obj = new Project();

        public static JsonObject set(String name
                , String description
                , String user_type
                , String[] hosts
                , String[] subscribed_topics
                , String[] publish_permissions
                , String[] subscribe_permissions) {
            return obj.put(Config.key_name, name)
                    .put(Config.key_description, description)
                    .put(Config.key_user_type, user_type)
                    .put(Config.key_hosts, hosts)
                    .put(Config.key_subscribed_topics, subscribed_topics)
                    .put(Config.key_publish_permissions, publish_permissions)
                    .put(Config.key_subscribe_permissions, subscribe_permissions);
        }
    }
    
}
