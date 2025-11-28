package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manage.TaskManager;
import tasks.SubTask;
import java.io.IOException;

public class SubTasksHandler extends BaseHttpHandler {

    public SubTasksHandler(TaskManager manager, Gson gson) {
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

        // GET /tasks/subtask?id=3
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            SubTask subTask = manager.getSubTask(id);
            if (subTask == null) {
                sendNotFound(exchange, "SubTask not found");
            } else {
                sendJson(exchange, subTask);
            }
            return;
        }

        // GET /tasks/subtask — получить все
        sendJson(exchange, manager.getSubTasks());
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        SubTask subTask = gson.fromJson(body, SubTask.class);

        if (subTask.getId() == 0) {
            manager.addSubTask(subTask);
            sendText(exchange, 201, "SubTask created");
        } else {
            manager.updateSubTask(subTask);
            sendText(exchange, 200, "SubTask updated");
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();

        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));
            manager.deleteSubTaskById(id);
            sendText(exchange, 200, "SubTask deleted");
        } else {
            manager.deleteAllSubTasks();
            sendText(exchange, 200,"All SubTasks deleted");
        }
    }
}
