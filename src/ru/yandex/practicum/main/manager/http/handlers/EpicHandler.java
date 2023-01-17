package manager.http.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import manager.managers.TaskManager;
import models.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static java.net.HttpURLConnection.*;

public class EpicHandler extends TaskHandler {

    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int rCode = HTTP_NOT_FOUND;
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET": {
                if (exchange.getRequestURI().getQuery() == null) {
                    if (manager.getEpicList().isEmpty()) {
                        response = "Список эпиков пуст";
                        rCode = HTTP_NO_CONTENT;
                    } else {
                        response = gson.toJson(manager.getEpicList());
                        rCode = HTTP_OK;
                    }
                } else {
                    Optional<Integer> taskIdOpt = getTaskId(exchange);
                    if (taskIdOpt.isEmpty()) {
                        response = "Некорректный идентификатор эпика";
                        rCode = HTTP_BAD_REQUEST;
                    } else {
                        int taskId = taskIdOpt.get();
                        response = "Эпик с идентификатором " + taskId + " не найден";
                        List<Epic> taskList = manager.getEpicList();
                        for (Epic task : taskList) {
                            if (task.getId() == taskId) {
                                response = gson.toJson(manager.getEpicById(taskId));
                                rCode = HTTP_OK;
                            }
                        }
                    }
                }
            }
            break;
            case "POST": {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                List<Epic> taskList = manager.getEpicList();
                try {
                    Epic bodyTask = gson.fromJson(body, Epic.class);
                    int taskId = bodyTask.getId();
                    if (taskId != 0) {
                        for (Epic task : taskList) {
                            response = "Эпик с id = " + taskId + " не найден";
                            rCode = HTTP_NOT_FOUND;
                            if (task.getId() == taskId) {
                                manager.updateEpic(taskId, task);
                                response = "Эпик с id = " + taskId + " обновлен";
                                rCode = HTTP_CREATED;
                            }
                        }
                    } else {
                        manager.addEpic(bodyTask);
                        response = "Эпик добавлен";
                        rCode = HTTP_CREATED;
                    }

                } catch (JsonSyntaxException exp) {
                    response = "Получен некорректный JSON";
                    rCode = HTTP_BAD_REQUEST;
                }
            }
            break;
            case "DELETE": {
                if (exchange.getRequestURI().getQuery() == null) {
                    manager.clearEpics();
                    response = "Лист эпиков пуст";
                    rCode = HTTP_OK;
                } else {
                    Optional<Integer> taskIdOpt = getTaskId(exchange);
                    if (taskIdOpt.isEmpty()) {
                        response = "Некорректный идентификатор эпика";
                        rCode = HTTP_BAD_REQUEST;
                    } else {
                        int taskId = taskIdOpt.get();
                        try {
                            manager.deleteEpicById(taskId);
                            response = "Эпик с id = " + taskId + " удален";
                            rCode = HTTP_OK;
                        } catch (RuntimeException exp) {
                            response = "Эпик с id = " + taskId + "не найден";
                        }
                    }
                }
            }
            break;
            default: {
                response = "Такого эндпоинта не существует";
            }
        }
        writeResponse(exchange, response, rCode);
    }

}
