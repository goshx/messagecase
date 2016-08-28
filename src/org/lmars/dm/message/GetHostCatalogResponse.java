package org.lmars.dm.message;

import io.vertx.core.json.JsonObject;
import org.lmars.dm.conf.Config;


public class GetHostCatalogResponse extends OkResponse2 {
    
    public GetHostCatalogResponse set(Host[] hosts) {
        put(Config.key_users, hosts);
        return this;
    }
    
    public static class Host extends JsonObject{

        static Host obj = new Host();

        public static JsonObject set(String ip
                , Project[] projects) {
            return obj.put(Config.key_ip, ip)
                    .put(Config.key_projects, projects);
        }
    }
    
    public static class Project extends JsonObject{

        static Project obj = new Project();

        public static JsonObject set(String name
                , String[] topics) {
            return obj.put(Config.key_name, name)
                    .put(Config.key_topics, topics);
        }
    }
    
}
