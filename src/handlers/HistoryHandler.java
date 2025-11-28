package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manage.TaskManager;
import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            sendText(exchange, 405,"Только GET поддерживается");
            return;
        }

        sendJson(exchange, manager.getHistory());
    }
}
