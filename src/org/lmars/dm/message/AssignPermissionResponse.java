package org.lmars.dm.message;

import io.vertx.core.json.JsonObject;
import org.lmars.dm.conf.Config;

public class AssignPermissionResponse extends OkResponse2 {
    
    public AssignPermissionResponse set(String username, Project[] projects) {
        put(Config.key_username, username)
                .put(Config.key_projects, projects);
        return this;
    }

    public static class Project extends JsonObject {

        static Project obj = new Project();

        public static JsonObject set(String user_type
                , String name
                , String[] hosts
                , String[] publish_permissions
                , String[] subscribe_permissions) {
            return obj.put(Config.key_user_type, user_type)
                    .put(Config.key_name, name)
                    .put(Config.key_hosts, hosts)
                    .put(Config.key_publish_permissions, publish_permissions)
                    .put(Config.key_subscribe_permissions, subscribe_permissions);
        }
    }
    
}
