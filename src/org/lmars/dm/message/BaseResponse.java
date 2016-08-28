package org.lmars.dm.message;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.time.Instant;
import org.lmars.dm.conf.Config;
import org.lmars.dm.util.RandomSessionID;

public abstract class BaseResponse {

    private JsonObject obj;

    public BaseResponse() {
    	obj = new JsonObject();
//        obj.put(Config.key_id, RandomSessionID.getUUID());
        init();
    }

    public BaseResponse(String id) {
    	obj = new JsonObject();
        obj.put(Config.key_id, id);
        init();
    }

    public abstract void init();

    public BaseResponse put(String key, Enum value) {
        obj.put(key, value);
        return this;
    }

    public BaseResponse put(String key, CharSequence value) {
        obj.put(key, value);
        return this;
    }

    public BaseResponse put(String key, String value) {
        obj.put(key, value);
        return this;
    }

    public BaseResponse put(String key, Integer value) {
        obj.put(key, value);
        return this;
    }

    public BaseResponse put(String key, Long value) {
        obj.put(key, value);
        return this;
    }

    public BaseResponse put(String key, Double value) {
        obj.put(key, value);
        return this;
    }

    public BaseResponse put(String key, Float value) {
        obj.put(key, value);
        return this;
    }

    public BaseResponse put(String key, Boolean value) {
        obj.put(key, value);
        return this;
    }

    public BaseResponse putNull(String key) {
        obj.putNull(key);
        return this;
    }

    public BaseResponse put(String key, JsonObject value) {
        obj.put(key, value);
        return this;
    }

    public BaseResponse put(String key, JsonArray value) {
        obj.put(key, value);
        return this;
    }

    public BaseResponse put(String key, byte[] value) {
        obj.put(key, value);
        return this;
    }

    public BaseResponse put(String key, Instant value) {
        obj.put(key, value);
        return this;
    }
    
    public BaseResponse put(String key, String[] value) {
        JsonArray arr = new JsonArray();
        for(String v:value) {
            arr.add(v);
        }
        obj.put(key, arr);
        return this;
    }

    public BaseResponse put(String key, Object value) {
        obj.put(key, value);
        return this;
    }

    public BaseResponse remove(String key) {
        obj.remove(key);
        return this;
    }

    public JsonObject create() {
        return obj;
    }

}
