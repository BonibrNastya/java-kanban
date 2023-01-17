package manager.http.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import manager.managers.TaskManager;
import models.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static java.net.HttpURLConnection.*;

public class SubHandler extends TaskHandler {

    public SubHandler(TaskManager manager) {
        super(manager);

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int rCode = HTTP_NOT_FOUND;
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET" : {
                if (exchange.getRequestURI().getQuery() == null) {
                    if (manager.getSubtaskList().isEmpty()) {
                        response = "Список сабтасок пуст";
                        rCode = HTTP_NO_CONTENT;
                    } else {
                        response = gson.toJson(manager.getSubtaskList());
                        rCode = HTTP_OK;
                    }
                } else {
                    Optional<Integer> taskIdOpt = getTaskId(exchange);
                    if (taskIdOpt.isEmpty()) {
                        response = "Некорректный идентификатор сабтаски";
                        rCode = HTTP_BAD_REQUEST;
                    } else {
                        int taskId = taskIdOpt.get();
                        response = "Сабтаска с идентификатором " + taskId + " не найдена";
                        List<Subtask> taskList = manager.getSubtaskList();
                        for (Subtask task : taskList) {
                            if (task.getId() == taskId) {
                                response = gson.toJson(manager.getSubtaskById(taskId));
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
                List<Subtask> taskList = manager.getSubtaskList();
                try {
                    Subtask bodyTask = gson.fromJson(body, Subtask.class);
                    int taskId = bodyTask.getId();
                    if (taskId != 0) {
                        for (Subtask task : taskList) {
                            response = "Сабтаска с id = " + taskId + " не найдена";
                            rCode = HTTP_NOT_FOUND;
                            if (task.getId() == taskId) {
                                manager.updateSubtask(taskId, task);
                                response = "Сабтаска с id = " + taskId + " обновлена";
                                rCode = HTTP_CREATED;
                            }
                        }
                    } else {
                        manager.addSubtask(bodyTask);
                        response = "Сабтаска добавлена";
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
                    manager.clearSubtasks();
                    response = "Лист сабтасок пуст";
                    rCode = HTTP_OK;
                } else {
                    Optional<Integer> taskIdOpt = getTaskId(exchange);
                    if (taskIdOpt.isEmpty()) {
                        response = "Некорректный идентификатор сабтаски";
                        rCode = HTTP_BAD_REQUEST;
                    } else {
                        int taskId = taskIdOpt.get();
                        try {
                            manager.deleteSubtaskById(taskId);
                            response = "Сабтаска с id = " + taskId + " удалена";
                            rCode = HTTP_OK;
                        } catch (RuntimeException exp) {
                            response = "Сабтаска с id = " + taskId + "не найдена";
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

}
