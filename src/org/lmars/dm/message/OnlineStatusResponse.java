package org.lmars.dm.message;

import org.lmars.dm.conf.Config;


public class OnlineStatusResponse extends OkResponse2 {
    
    public OnlineStatusResponse set(String[] online_users, String[] offline_users) {
        put(Config.key_online_users, online_users)
                .put(Config.key_offline_users, offline_users);
        return this;
    }
    
}
