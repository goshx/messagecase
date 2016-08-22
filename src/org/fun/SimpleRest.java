package org.fun;

import com.hazelcast.util.function.Consumer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

public class SimpleRest extends AbstractVerticle {

    public static void main(String[] args) {
        init();
    }

    private static void init() {
        VertxOptions vertxOptions = new VertxOptions();
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        String verticleID = SimpleRest.class.getName();
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

    @Override
    public void start() {

        setUpInitialData();

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.get("/products").handler(this::handleListProducts);
        router.get("/setproduct").handler(this::handleAddProduct);
        router.get("/getproduct").handler(this::handleGetProduct);
        router.get("/delproduct").handler(this::handleDelProduct);

        vertx.createHttpServer().requestHandler(router::accept).listen(7080);
    }

    private void handleGetProduct(RoutingContext routingContext) {
        String productID = routingContext.request().getParam("productID");
        HttpServerResponse response = routingContext.response();
        if (productID == null) {
            sendError(400, response);
        } else {
            JsonObject product = products.get(productID);
            if (product == null) {
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(product.encodePrettily());
            }
        }
    }

    private void handleAddProduct(RoutingContext routingContext) {
        String productID = routingContext.request().getParam("productID");
        String productContent = routingContext.request().getParam("content");
        HttpServerResponse response = routingContext.response();
        try {
            if (productID == null || productContent == null) {
                sendError(400, response);
            } else {
                JsonObject product = new JsonObject(productContent);
                product.put("id", productID);
                if (product == null) {
                    sendError(400, response);
                } else {
                    products.put(productID, product);
                    JsonArray arr = new JsonArray();
                    products.forEach((k, v) -> arr.add(v));
                    reply(routingContext.response(), arr.encodePrettily());
                }
            }
        } catch (Exception e) {
            reply(response, e.getMessage());
        }
    }

    private void handleDelProduct(RoutingContext routingContext) {
        String productID = routingContext.request().getParam("productID");
        HttpServerResponse response = routingContext.response();
        try {
            if (productID == null) {
                sendError(400, response);
            } else {
                products.remove(productID);
                JsonArray arr = new JsonArray();
                products.forEach((k, v) -> arr.add(v));
                reply(routingContext.response(), arr.encodePrettily());
            }
        } catch (Exception e) {
            reply(response, e.getMessage());
        }
    }

    private void handleListProducts(RoutingContext routingContext) {
        JsonArray arr = new JsonArray();
        products.forEach((k, v) -> arr.add(v));
        reply(routingContext.response(), arr.encodePrettily());
    }

    private void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }

    private void setUpInitialData() {
        addProduct(new JsonObject().put("id", "prod3568").put("name", "Egg Whisk").put("price", 3.99).put("weight", 150));
        addProduct(new JsonObject().put("id", "prod7340").put("name", "Tea Cosy").put("price", 5.99).put("weight", 100));
        addProduct(new JsonObject().put("id", "prod8643").put("name", "Spatula").put("price", 1.00).put("weight", 80));
    }

    private void reply(HttpServerResponse res, String content) {
        res.putHeader("content-type", "application/json").end(content);
    }

    private void addProduct(JsonObject product) {
        products.put(product.getString("id"), product);
    }

    private Map<String, JsonObject> products = new HashMap<>();
}
