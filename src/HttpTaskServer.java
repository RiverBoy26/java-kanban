import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import handlers.*;
import manage.TaskManager;
import manage.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import com.sun.net.httpserver.HttpServer;

public class HttpTaskServer {
    private static final int PORT = 8080;

    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        TaskManager taskManager = Managers.getDefault();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .serializeNulls()
                .create();

        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TasksHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubTasksHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicsHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на порту " + PORT);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("HTTP-сервер остановлен.");
    }

    public static void main(String[] args) {
        try {
            new HttpTaskServer().start();
        } catch (Exception e) {
            System.out.println("Ошибка запуска сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }
}