package manager.http.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.http.LocalDateAdapter;
import manager.managers.TaskManager;
import models.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.net.HttpURLConnection.*;

public class TaskHandler implements HttpHandler {
    protected final TaskManager manager;
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .create();

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int rCode = HTTP_NOT_FOUND;
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET" : {
                if (exchange.getRequestURI().getQuery() == null) {

                    if (manager.getTaskList().isEmpty()) {
                        response = "Список тасок пуст";
                        rCode = HTTP_NO_CONTENT;
                    } else {
                        response = gson.toJson(manager.getTaskList());
                        rCode = HTTP_OK;
                    }
                } else {
                    Optional<Integer> taskIdOpt = getTaskId(exchange);
                    if (taskIdOpt.isEmpty()) {
                        response = "Некорректный идентификатор таски";
                        rCode = HTTP_BAD_REQUEST;
                    } else {
                        int taskId = taskIdOpt.get();
                        response = "Таска с идентификатором " + taskId + " не найдена";
                        List<Task> taskList = manager.getTaskList();
                        for (Task task : taskList) {
                            if (task.getId() == taskId) {
                                response = gson.toJson(manager.getTaskById(taskId));
                                rCode = HTTP_OK;
                            }
                        }
                    }
                }
            }
            break;
            case "POST" : {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                List<Task> taskList = manager.getTaskList();
                try {
                    Task bodyTask = gson.fromJson(body, Task.class);
                    int taskId = bodyTask.getId();
                    if (taskId != 0) {
                        for (Task task : taskList) {
                            response = "Таска с id = " + taskId + " не найдена";
                            rCode = HTTP_NOT_FOUND;
                            if (task.getId() == taskId) {
                                manager.updateTask(taskId, task);
                                response = "Таска с id = " + taskId + " обновлена";
                                rCode = HTTP_CREATED;
                            }
                        }
                    } else {
                        manager.addTask(bodyTask);
                        response = "Таска добавлена";
                        rCode = HTTP_CREATED;
                    }

                } catch (JsonSyntaxException exp) {
                    response = "Получен некорректный JSON";
                    rCode = HTTP_BAD_REQUEST;
                }
            }
            break;
            case "DELETE" : {
                if (exchange.getRequestURI().getQuery() == null) {
                    manager.clearTasks();
                    response = "Лист тасок пуст";
                    rCode = HTTP_OK;
                } else {
                    Optional<Integer> taskIdOpt = getTaskId(exchange);
                    if (taskIdOpt.isEmpty()) {
                        response = "Некорректный идентификатор таски";
                        rCode = HTTP_BAD_REQUEST;
                    } else {
                        int taskId = taskIdOpt.get();
                        try {
                            manager.deleteTaskById(taskId);
                            response = "Таска с id = " + taskId + " удалена";
                            rCode = HTTP_OK;
                        } catch (RuntimeException exp) {
                            response = "Таска с id = " + taskId + "не найдена";
                        }
                    }
                }
            }
            break;
            default : {
                response = "Такого эндпоинта не существует";
            }
        }
        writeResponse(exchange, response, rCode);
    }


    protected Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getQuery().split("=");
        try {
            return Optional.of(Integer.parseInt(pathParts[1]));
        } catch (ArrayIndexOutOfBoundsException exception) {
            return Optional.empty();
        }
    }

    protected void writeResponse(HttpExchange exchange,
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