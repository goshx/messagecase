package org.lmars.dm.message;


import org.lmars.dm.conf.Config;

public class ErrorResponse2 extends BaseResponse {

    @Override
    public void init() {
        put(Config.key_type, Config.response_type.error);
    }

    public ErrorResponse2 set(String message) {
        put(Config.key_message, message);
        return this;
    }

}
