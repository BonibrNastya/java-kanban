package manager.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.managers.InMemoryHistoryManager;
import manager.managers.TaskManager;
import models.Task;

import java.io.IOException;
import java.util.List;

import static java.net.HttpURLConnection.*;


public class HistoryHandler extends TaskHandler {
    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            List<Task> historyManager = new InMemoryHistoryManager().getHistory();
            if (historyManager.size() == 0) {
                writeResponse(exchange, "Список истории пуст", HTTP_NOT_FOUND);
                return;
            }
            writeResponse(exchange, gson.toJson(historyManager), HTTP_OK);
        } else {
            writeResponse(exchange, "Такого эндпоинта не существует", HTTP_NOT_FOUND);
        }
    }
}