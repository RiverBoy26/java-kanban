package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manage.TaskManager;
import tasks.Epic;
import java.io.IOException;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                handleGet(exchange);
                break;
            case "POST":
                handlePost(exchange);
                break;
            case "DELETE":
                handleDelete(exchange);
                break;
            default:
                sendText(exchange, 405, "Метод не поддерживается");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        // GET /tasks/epic?id=3
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            Epic epic = manager.getEpic(id);

            if (epic == null) {
                sendNotFound(exchange, "Epic not found");
            } else {
                sendJson(exchange, epic);
            }
            return;
        }

        // GET /tasks/epics — получить все эпики
        sendJson(exchange, manager.getEpics());
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        Epic epic = gson.fromJson(body, Epic.class);

        if (epic.getId() == 0) {
            manager.addEpic(epic);
            sendText(exchange, 201, "Epic created");
        } else {
            manager.updateEpic(epic);
            sendText(exchange, 200, "Epic updated");
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        // DELETE /tasks/epic?id=3
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            manager.deleteEpicById(id);
            sendText(exchange, 200, "Epic deleted");
        } else {
            manager.deleteAllEpics();
            sendText(exchange, 200, "All Epics deleted");
        }
    }
}