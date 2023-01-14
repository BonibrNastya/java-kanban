package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import models.*;
import utils.Managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new Gson();

    public HttpTaskServer() throws IOException, InterruptedException {

        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

    }

    static class TasksHandler implements HttpHandler {

        private final TaskManager manager;

        public TasksHandler() throws IOException, InterruptedException {
            this.manager = Managers.getDefaultHttpManager();
        }


        @Override
        public void handle(HttpExchange exchange) throws IOException {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
            gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
            gson = gsonBuilder.create();

            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestURI().getQuery(), exchange.getRequestMethod());

            switch (endpoint) {
                case GET_PRIORITIZED -> handleGetPrioritized(exchange);
                case GET_TASK -> handleGetTask(exchange);
                case GET_EPIC -> handleGetEpic(exchange);
                case GET_SUB -> handleGetSub(exchange);
                case GET_HISTORY -> handleGetHistory(exchange);
                case GET_TASK_BY_ID -> handleGetTaskById(exchange);
                case GET_EPIC_BY_ID -> handleGetEpicById(exchange);
                case GET_SUB_BY_ID -> handleGetSubtaskById(exchange);
                case GET_EPIC_SUB -> handleGetEpicSubs(exchange);
                case POST_TASK -> handlePostTask(exchange);
                case POST_EPIC -> handlePostEpic(exchange);
                case POST_SUB -> handlePostSub(exchange);
                case DELETE_TASK -> handleDelTask(exchange);
                case DELETE_EPIC -> handleDelEpic(exchange);
                case DELETE_SUB -> handleDelSub(exchange);
                case DELETE_TASK_BY_ID -> handleDelTaskById(exchange);
                case DELETE_EPIC_BY_ID -> handleDelEpicById(exchange);
                case DELETE_SUB_BY_ID -> handleDelSubById(exchange);
            }
        }

        private void handlePostTask(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            List<Task> taskList = manager.getTaskList();

            try {
                Task bodyTask = gson.fromJson(body, Task.class);
                try {
                    int taskId = bodyTask.getId();
                    if (taskId == 0) {
                        manager.addTask(bodyTask);
                        writeResponse(exchange, "Таска добавлена", 201);
                        return;
                    }
                    for (Task task : taskList) {
                        if (task.getId() == taskId) {
                            manager.updateTask(taskId, task);
                            writeResponse(exchange, "Таска с id = " + taskId + " обновлена", 201);
                            return;
                        }
                    }
                    writeResponse(exchange, "Таска с id = " + taskId + " не найдена", 404);

                } catch (NullPointerException exp) {
                    writeResponse(exchange, "Поля таски не должны быть пустыми", 400);
                }

            } catch (JsonSyntaxException exp) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
            }
        }

        private void handlePostSub(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            List<Subtask> taskList = manager.getSubtaskList();

            try {
                Subtask bodyTask = gson.fromJson(body, Subtask.class);
                try {
                    int taskId = bodyTask.getId();
                    if (taskId == 0) {
                        manager.addSubtask(bodyTask);
                        writeResponse(exchange, "Сабтаска добавлена", 201);
                        return;
                    }
                    for (Subtask task : taskList) {
                        if (task.getId() == taskId) {
                            manager.updateSubtask(taskId, task);
                            writeResponse(exchange, "Сабтаска с id = " + taskId + " обновлена", 201);
                            return;
                        }
                    }
                    writeResponse(exchange, "Сабтаска с id = " + taskId + " не найдена", 404);

                } catch (NullPointerException exp) {
                    writeResponse(exchange, "Поля сабтаски не должны быть пустыми", 400);
                }

            } catch (JsonSyntaxException exp) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
            }
        }

        private void handlePostEpic(HttpExchange exchange) throws IOException {
            InputStream inputStream = exchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            List<Epic> taskList = manager.getEpicList();

            try {
                Epic bodyTask = gson.fromJson(body, Epic.class);
                try {
                    int taskId = bodyTask.getId();
                    if (taskId == 0) {
                        manager.addEpic(bodyTask);
                        writeResponse(exchange, "Эпик добавлен", 201);
                        return;
                    }
                    for (Epic task : taskList) {
                        if (task.getId() == taskId) {
                            manager.updateEpic(taskId, task);
                            writeResponse(exchange, "Эпик с id = " + taskId + " обновлен", 201);
                            return;
                        }
                    }
                    writeResponse(exchange, "Эпик с id = " + taskId + " не найден", 404);

                } catch (NullPointerException exp) {
                    writeResponse(exchange, "Поля эпика не должны быть пустыми", 400);
                }

            } catch (JsonSyntaxException exp) {
                writeResponse(exchange, "Получен некорректный JSON", 400);
            }
        }


        private void handleDelTaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор таски", 400);
                return;
            }
            int taskId = taskIdOpt.get();
            try {
                manager.deleteTaskById(taskId);
                writeResponse(exchange, "Таска с id = " + taskId + " удалена", 200);
            } catch (RuntimeException exp) {
                writeResponse(exchange, "Таска с id = " + taskId + "не найдена", 404);
            }
        }

        private void handleDelEpicById(HttpExchange exchange) throws IOException {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор эпика", 400);
                return;
            }
            int taskId = taskIdOpt.get();
            try {
                manager.deleteEpicById(taskId);
                writeResponse(exchange, "Эпик с id = " + taskId + " удален", 200);
            } catch (RuntimeException exp) {
                writeResponse(exchange, "Эпик с id = " + taskId + "не найден", 404);
            }
        }

        private void handleDelSubById(HttpExchange exchange) throws IOException {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор сабтаски", 400);
                return;
            }
            int taskId = taskIdOpt.get();
            try {
                manager.deleteSubtaskById(taskId);
                writeResponse(exchange, "Сабтаска с id = " + taskId + " удалена", 200);
            } catch (RuntimeException exp) {
                writeResponse(exchange, "Сабтаска с id = " + taskId + "не найдена", 404);
            }
        }

        private void handleDelTask(HttpExchange exchange) throws IOException {
            manager.clearTasks();
            writeResponse(exchange, "Лист тасок пуст", 200);
        }

        private void handleDelSub(HttpExchange exchange) throws IOException {
            manager.clearSubtasks();
            writeResponse(exchange, "Лист сабтасок пуст", 200);
        }

        private void handleDelEpic(HttpExchange exchange) throws IOException {
            manager.clearEpics();
            writeResponse(exchange, "Лист эпиков пуст", 200);
        }

        private void handleGetEpicSubs(HttpExchange exchange) throws IOException {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор эпика для получения списка сабтасок", 400);
                return;
            }
            int taskId = taskIdOpt.get();
            try {
                List<Subtask> subtaskList = manager.getAllSubtasksFromEpic(taskId);
                writeResponse(exchange, gson.toJson(subtaskList), 200);
            } catch (RuntimeException exp) {
                writeResponse(exchange, "Эпик с id = " + taskId + " отсутствует, либо пуст", 404);
            }
        }

        private void handleGetPrioritized(HttpExchange exchange) throws IOException {
            if (InMemoryTaskManager.getPrioritizedTasks().isEmpty()) {
                writeResponse(exchange, "Список приоритетных тасок пуст", 404);
                return;
            }
            writeResponse(exchange, gson.toJson(InMemoryTaskManager.getPrioritizedTasks()), 200);
        }

        private void handleGetTask(HttpExchange exchange) throws IOException {
            if (manager.getTaskList().isEmpty()) {
                writeResponse(exchange, "Список тасок пуст", 404);
                return;
            }
            writeResponse(exchange, gson.toJson(manager.getTaskList()), 200);
        }

        private void handleGetEpic(HttpExchange exchange) throws IOException {
            if (manager.getEpicList().isEmpty()) {
                writeResponse(exchange, "Список эпиков пуст", 404);
                return;
            }
            writeResponse(exchange, gson.toJson(manager.getEpicList()), 200);
        }

        private void handleGetSub(HttpExchange exchange) throws IOException {
            if (manager.getSubtaskList().isEmpty()) {
                writeResponse(exchange, "Список сабтасок пуст", 404);
                return;
            }
            writeResponse(exchange, gson.toJson(manager.getSubtaskList()), 200);
        }

        private void handleGetHistory(HttpExchange exchange) throws IOException {
            List<Task> historyManager = new InMemoryHistoryManager().getHistory();
            if (historyManager.size() == 0) {
                writeResponse(exchange, "Список истории пуст", 404);
                return;
            }
            writeResponse(exchange, gson.toJson(historyManager), 200);
        }

        private void handleGetTaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор таски", 400);
                return;
            }
            int taskId = taskIdOpt.get();
            List<Task> taskList = manager.getTaskList();

            for (Task task : taskList) {
                if (task.getId() == taskId) {
                    String taskById = gson.toJson(manager.getTaskById(taskId));
                    writeResponse(exchange, taskById, 200);
                    return;
                }
            }
            writeResponse(exchange, "Таска с идентификатором " + taskId + " не найдена", 404);
        }

        private void handleGetEpicById(HttpExchange exchange) throws IOException {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор эпика", 400);
                return;
            }
            int taskId = taskIdOpt.get();
            List<Epic> taskList = manager.getEpicList();

            for (Epic task : taskList) {
                if (task.getId() == taskId) {
                    String taskById = gson.toJson(manager.getEpicById(taskId));
                    writeResponse(exchange, taskById, 200);
                    return;
                }
            }
            writeResponse(exchange, "Эпик с идентификатором " + taskId + " не найден", 404);
        }

        private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор сабтаски", 400);
                return;
            }
            int taskId = taskIdOpt.get();
            List<Subtask> taskList = manager.getSubtaskList();

            for (Subtask task : taskList) {
                if (task.getId() == taskId) {
                    String taskById = gson.toJson(manager.getSubtaskById(taskId));
                    writeResponse(exchange, taskById, 200);
                    return;
                }
            }
            writeResponse(exchange, "Сабтаска с идентификатором " + taskId + " не найдена", 404);
        }

        private Endpoint getEndpoint(String requestPath, String requestQuery, String requestMethod) {
            String[] pathParts = requestPath.split("/");
//            String[] queryParts = requestQuery.split("=");

            if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                return Endpoint.GET_PRIORITIZED;
            }
            if (pathParts.length == 3 && pathParts[1].equals("tasks") && requestQuery == null) {
                if (requestMethod.equals("GET")) {
                    if (pathParts[2].equals("task")) {
                        return Endpoint.GET_TASK;
                    }
                    if (pathParts[2].equals("epic")) {
                        return Endpoint.GET_EPIC;
                    }
                    if (pathParts[2].equals("subtask")) {
                        return Endpoint.GET_SUB;
                    }
                    if (pathParts[2].equals("history")) {
                        return Endpoint.GET_HISTORY;
                    }
                }
                if (requestMethod.equals("DELETE")) {
                    if (pathParts[2].equals("task")) {
                        return Endpoint.DELETE_TASK;
                    }
                    if (pathParts[2].equals("epic")) {
                        return Endpoint.DELETE_EPIC;
                    }
                    if (pathParts[2].equals("subtask")) {
                        return Endpoint.DELETE_SUB;
                    }
                }
                if (requestMethod.equals("POST")) {
                    if (pathParts[2].equals("task")) {
                        return Endpoint.POST_TASK;
                    }
                    if (pathParts[2].equals("epic")) {
                        return Endpoint.POST_EPIC;
                    }
                    if (pathParts[2].equals("subtask")) {
                        return Endpoint.POST_SUB;
                    }
                }
            }
            if (pathParts.length == 3 && pathParts[1].equals("tasks") && requestQuery != null) {
                if (requestMethod.equals("GET")) {
                    if (pathParts[2].equals("task")) {
                        return Endpoint.GET_TASK_BY_ID;
                    }
                    if (pathParts[2].equals("epic")) {
                        return Endpoint.GET_EPIC_BY_ID;
                    }
                    if (pathParts[2].equals("subtask")) {
                        return Endpoint.GET_SUB_BY_ID;
                    }
                }
                if (requestMethod.equals("DELETE")) {
                    if (pathParts[2].equals("task")) {
                        return Endpoint.DELETE_TASK_BY_ID;
                    }
                    if (pathParts[2].equals("epic")) {
                        return Endpoint.DELETE_EPIC_BY_ID;
                    }
                    if (pathParts[2].equals("subtask")) {
                        return Endpoint.DELETE_SUB_BY_ID;
                    }
                }
            }
            if (pathParts.length == 4 && pathParts[1].equals("tasks") && pathParts[2].equals("subtask") && pathParts[3].equals("epic") && requestQuery != null) {
                return Endpoint.GET_EPIC_SUB;
            }
            return Endpoint.UNKNOWN;
        }

        private Optional<Integer> getTaskId(HttpExchange exchange) {
            String[] pathParts = exchange.getRequestURI().getQuery().split("=");
            try {
                return Optional.of(Integer.parseInt(pathParts[1]));
            } catch (ArrayIndexOutOfBoundsException exception) {
                return Optional.empty();
            }
        }

        private void writeResponse(HttpExchange exchange,
                                   String responseString,
                                   int responseCode) throws IOException {
            if (responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }
    }

    static class DurationAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
            jsonWriter.value(duration.toMinutes());
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            String durationString = jsonReader.nextString();
            if (durationString != null) {
                return Duration.ofMinutes(Long.parseLong(durationString));
            }
            return null;
        }
    }

    static class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
        //   private static final DateTimeFormatter formatterWrite = DateTimeFormatter.ofPattern("dd--MM--yyyy");
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
            if (localDateTime != null) {
                jsonWriter.value(localDateTime.format(formatter));
            }
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            if (jsonReader != null) {
                return LocalDateTime.parse(jsonReader.nextString(), formatter);
            }
            return null;
        }
    }
}
