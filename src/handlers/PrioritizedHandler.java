package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manage.TaskManager;
import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (method.equals("GET")) {
            handleGet(exchange);
        } else {
            exchange.sendResponseHeaders(405, 0);
            exchange.getResponseBody().close();
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        var tasks = manager.getPrioritizedTasks();
        sendJson(exchange, tasks);
    }
}