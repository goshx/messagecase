package org.fun.web;

import com.hazelcast.util.function.Consumer;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Runer {

    public static void run(Class classz) {
        VertxOptions vertxOptions = new VertxOptions();
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        String verticleID = classz.getName();
        Consumer<Vertx> runner = vertx -> {
            try {
                vertx.deployVerticle(verticleID, deploymentOptions);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        };
        Vertx vertx = Vertx.vertx(vertxOptions);
        runner.accept(vertx);
    }

}
