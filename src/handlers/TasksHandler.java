package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manage.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TasksHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public TasksHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] split = path.split("/");

        try {
            switch (method) {
                case "GET":
                    handleGet(exchange, split);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange, split);
                    break;
                default:
                    sendText(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            sendText(exchange, 500, "Server error: " + e.getMessage());
        }
    }

    private void handleGet(HttpExchange exchange, String[] split) throws IOException {
        if (split.length == 2) {
            sendJson(exchange, manager.getTasks());
            return;
        }

        if (split.length == 3) {
            int id = Integer.parseInt(split[2]);
            Task task = manager.getTask(id);

            if (task == null) {
                sendText(exchange, 404, "Task not found");
            } else {
                sendJson(exchange, task);
            }
            return;
        }

        sendText(exchange, 400, "Bad request");
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);

        Task task = gson.fromJson(body, Task.class);

        if (task.getId() == 0) {
            manager.addTask(task);
            sendText(exchange, 201, "Task created");
        } else {
            manager.updateTask(task);
            sendText(exchange, 200, "Task updated");
        }
    }

    private void handleDelete(HttpExchange exchange, String[] split) throws IOException {
        if (split.length == 2) {
            manager.deleteAllTasks();
            sendText(exchange, 200, "All tasks deleted");
            return;
        }

        if (split.length == 3) {
            int id = Integer.parseInt(split[2]);
            manager.deleteTaskById(id);
            sendText(exchange, 200, "Task deleted");
            return;
        }

        sendText(exchange, 400, "Bad request");
    }

    private void sendJson(HttpExchange exchange, Object data) throws IOException {
        String json = gson.toJson(data);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, json.getBytes().length);
        exchange.getResponseBody().write(json.getBytes());
        exchange.close();
    }

    private void sendText(HttpExchange exchange, int code, String text) throws IOException {
        exchange.sendResponseHeaders(code, text.getBytes().length);
        exchange.getResponseBody().write(text.getBytes());
        exchange.close();
    }
}
