package org.lmars.dm.message;

import org.lmars.dm.conf.Config;


public class OkResponse2 extends BaseResponse {

    @Override
    public void init() {
        put(Config.key_type, Config.response_type.ok);
    }
    
}
