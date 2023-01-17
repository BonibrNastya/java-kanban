package manager.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.managers.TaskManager;
import models.Subtask;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.net.HttpURLConnection.*;


public class SubByEpicHandler extends TaskHandler {
    public SubByEpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange,
                        "Некорректный идентификатор эпика для получения списка сабтасок",
                        HTTP_BAD_REQUEST);
                return;
            }
            int taskId = taskIdOpt.get();
            try {
                List<Subtask> subtaskList = manager.getAllSubtasksFromEpic(taskId);
                writeResponse(exchange, gson.toJson(subtaskList), HTTP_OK);
            } catch (RuntimeException exp) {
                writeResponse(exchange,
                        "Эпик с id = " + taskId + " отсутствует, либо пуст",
                        HTTP_NOT_FOUND);
            }
        } else {
            writeResponse(exchange, "Такого эндпоинта не существует", HTTP_NOT_FOUND);
        }
    }
}
