package org.lmars.dm.util;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public abstract class BaseServer extends AbstractVerticle{
    
    public static void create(Class classz, int server_worker_poor_size, long max_event_loop_execute_time) {
        VertxOptions options = new VertxOptions()
                .setWorkerPoolSize(server_worker_poor_size)
                .setMaxEventLoopExecuteTime(max_event_loop_execute_time);
        Vertx vertx = Vertx.factory.vertx(options);
        vertx.deployVerticle(classz.getName());
    }
    
    @Override
    public abstract void start() throws Exception;
    
}
