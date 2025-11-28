package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manage.TaskManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {

    protected final TaskManager manager;
    protected final Gson gson;

    protected BaseHttpHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public final void handle(HttpExchange exchange) throws IOException {
        try {
            handleRequest(exchange);
        } catch (Exception e) {
            sendError(exchange, e);
        } finally {
            try {
                exchange.close();
            } catch (Exception ignored) {
                // ignored
            }
        }
    }

    protected abstract void handleRequest(HttpExchange exchange) throws Exception;

    protected void sendText(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        byte[] bytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    protected void sendJson(HttpExchange exchange, Object obj) throws IOException {
        String json = gson.toJson(obj);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    protected void sendNotFound(HttpExchange exchange, String message) throws IOException {
        String json = gson.toJson(message);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(404, bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    protected void sendHasOverlaps(HttpExchange exchange, String message) throws IOException {
        String json = gson.toJson(message);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(409, bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    protected void sendError(HttpExchange exchange, Exception e) throws IOException {
        String json = gson.toJson("Internal server error: " + e.getMessage());
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(500, bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    protected String readBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}