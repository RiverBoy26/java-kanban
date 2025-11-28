package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import manage.InMemoryTaskManager;
import manage.TaskManager;
import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpHandlersTest {

    private static HttpServer server;
    private static int port;
    private static HttpClient client;
    private static TaskManager manager;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @BeforeAll
    static void setup() throws IOException {
        port = findFreePort();
        manager = new InMemoryTaskManager();
        client = HttpClient.newHttpClient();

        server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/tasks", new TasksHandler(manager, gson));
        server.createContext("/subtasks", new SubTasksHandler(manager, gson));
        server.createContext("/epics", new EpicsHandler(manager, gson));
        server.createContext("/history", new HistoryHandler(manager, gson));
        server.createContext("/prioritized", new PrioritizedHandler(manager, gson));

        server.start();
    }

    @AfterAll
    static void tearDown() {
        server.stop(0);
    }

    private static int findFreePort() throws IOException {
        try (var socket = new java.net.ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    @Order(1)
    void createTaskTest() throws Exception {
        Task t = new Task("A", "B");
        String body = gson.toJson(t);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url("/tasks")))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, resp.statusCode());
    }

    @Test
    @Order(2)
    void getTaskByIdTest() throws Exception {
        HttpResponse<String> resp = client.send(
                HttpRequest.newBuilder().uri(URI.create(url("/tasks"))).GET().build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("A"));
    }

    @Test
    @Order(3)
    void getAllTasksTest() throws Exception {
        HttpResponse<String> resp = client.send(
                HttpRequest.newBuilder().uri(URI.create(url("/tasks"))).GET().build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, resp.statusCode());
    }

    @Test
    @Order(4)
    void updateTaskTest() throws Exception {
        Task base = new Task("Original", "desc");
        HttpResponse<String> createResp = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(url("/tasks")))
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(base)))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(201, createResp.statusCode(), "Не удалось создать исходную задачу");

        // 2) Получаем ID созданной задачи
        Task created = manager.getTasks().get(0);

        Task t = new Task("Updated", "xxx");
        t.setId(1);

        HttpResponse<String> resp = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(url("/tasks")))
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(t)))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, resp.statusCode());
    }

    @Test
    @Order(5)
    void deleteTaskTest() throws Exception {
        HttpResponse<String> resp = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(url("/tasks/1")))
                        .DELETE()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, resp.statusCode());
    }

    @Test
    @Order(6)
    void createSubtaskTest() throws Exception {
        // 1) Создаём Epic, иначе SubTask некуда привязывать
        Epic epic = new Epic("E1", "desc");
        String epicBody = gson.toJson(epic);

        HttpResponse<String> epicResp = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(url("/epics")))
                        .POST(HttpRequest.BodyPublishers.ofString(epicBody))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(201, epicResp.statusCode());

        Epic createdEpic = manager.getEpics().get(0);
        assertNotNull(createdEpic);
        int epicId = createdEpic.getId();

        SubTask st = new SubTask("S1", "sd", createdEpic);

        String stBody = gson.toJson(st);

        HttpResponse<String> resp = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(url("/subtasks")))
                        .POST(HttpRequest.BodyPublishers.ofString(stBody))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(201, resp.statusCode());
    }

    @Test
    @Order(7)
    void getSubtaskByIdTest() throws Exception {
        HttpResponse<String> resp = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(url("/subtasks/2")))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, resp.statusCode());
    }

    @Test
    @Order(8)
    void createEpicTest() throws Exception {
        Epic epic = new Epic("Epic1", "desc");
        String body = gson.toJson(epic);

        HttpResponse<String> resp = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(url("/epics")))
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(201, resp.statusCode());
    }

    @Test
    @Order(9)
    void getEpicByIdTest() throws Exception {
        HttpResponse<String> resp = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(url("/epics/3")))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, resp.statusCode());
    }

    @Test
    @Order(10)
    void historyTest() throws Exception {
        HttpResponse<String> resp = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(url("/history")))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, resp.statusCode());
    }

    @Test
    @Order(11)
    void historyWrongMethodTest() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url("/history")))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, resp.statusCode(),
                "POST /history должен вернуть 405 Method Not Allowed");
    }

    @Test
    @Order(12)
    void testGetPrioritizedTasks() throws Exception {
        // Создаем задачу, чтобы она появилась в списке приоритета
        Task t = new Task("PrioTask", "desc");
        HttpRequest createReq = HttpRequest.newBuilder()
                .uri(URI.create(url("/tasks")))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(t)))
                .build();

        HttpResponse<String> createResp =
                client.send(createReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, createResp.statusCode());

        // Запрашиваем отсортированные задачи
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url("/prioritized")))
                .GET()
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, resp.statusCode());
        assertTrue(resp.body().contains("PrioTask"),
                "Ответ должен содержать созданную задачу");
    }

    @Test
    @Order(13)
    void testPrioritizedWrongMethod() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url("/prioritized")))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, resp.statusCode(),
                "POST /prioritized должен вернуть 405");
    }
}
