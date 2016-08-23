package org.fun.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

public class Chat extends AbstractVerticle{

    public static void main(String[] args) {
        Runer.run(Chat.class);
    }

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        MessageConsumer<String> consumer = eventBus.consumer("news.uk.sport");
        consumer.handler(message -> {
            System.out.println("I have received a message: " + message.body());
            message.reply("how interesting!");
        });

        eventBus.send("news.uk.sport", "Yay! Someone kicked a ball across a patch of grass", ar -> {
            if (ar.succeeded()) {
                System.out.println("Received reply: " + ar.result().body());
            } else {
                System.out.println(ar.cause().getLocalizedMessage());
            }
        });
    }
}
