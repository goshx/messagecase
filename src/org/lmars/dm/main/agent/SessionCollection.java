package org.lmars.dm.main.agent;

import io.vertx.core.json.JsonObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lmars.dm.conf.Config;

public class SessionCollection implements Runnable {

    private static SessionCollection collection = null;

    private static final ConcurrentMap<String, SessionInfo> session_mapper
            = new ConcurrentHashMap<>();

    public static SessionCollection helper() {
        if (collection == null) {
            collection = new SessionCollection();
        }
        return collection;
    }

    @Override
    public void run() {
        clear();
    }

    private class SessionInfo {

        public String user;
        public long time;
        public JsonObject more;

        public SessionInfo set(String user, long time) {
            this.user = user;
            this.time = time;
            return this;
        }
        
        public SessionInfo set(String user, long time, JsonObject more) {
            set(user, time).more = more;
            return this;
        }
        
    }

    public void init() {
        Executors.newScheduledThreadPool(Config.session_check_threadpool_size)
                .scheduleAtFixedRate(this
                , Config.session_check_delay
                , Config.session_check_period
                , TimeUnit.MILLISECONDS);
        Logger.getLogger(SessionCollection.class.getName())
                .log(Level.INFO, "[Session Collector] Starting in "
                        + Thread.currentThread().getName());
    }

    public ConcurrentMap<String, SessionInfo> add(String sessionId, String user, JsonObject more) {
        if(null==sessionId) return session_mapper;
        final SessionInfo session = new SessionInfo().set(user, System.currentTimeMillis(), more);
        session_mapper.put(sessionId, session);
        return session_mapper;
    }

    public ConcurrentMap<String, SessionInfo> update(String sessionId, String user, JsonObject more) {
        if(null==sessionId) return session_mapper;
        String u = (user == null ? get(sessionId).user : user);
        JsonObject m = (more == null ? get(sessionId).more : more);
        final SessionInfo session = new SessionInfo().set(u, System.currentTimeMillis(), m);
        session_mapper.put(sessionId, session);
        return session_mapper;
    }

    public ConcurrentMap<String, SessionInfo> remove(String sessionId) {
        if(null==sessionId) return session_mapper;
        session_mapper.remove(sessionId);
        return session_mapper;
    }

    public void clear() {
        session_mapper.forEach(new BiConsumer<String, SessionInfo>() {
            @Override
            public void accept(String s, SessionInfo u) {
                if (System.currentTimeMillis() - u.time > Config.session_alive_timespan) {
                    try {
                        remove(s);
                    } catch (Exception ex) {
                        Logger.getLogger(SessionCollection.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    public SessionInfo get(String sessionId) {
        return null==sessionId?null:session_mapper.get(sessionId);
    }

}
