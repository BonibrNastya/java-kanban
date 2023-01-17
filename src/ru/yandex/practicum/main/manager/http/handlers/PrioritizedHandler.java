package manager.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.managers.InMemoryTaskManager;
import manager.managers.TaskManager;

import java.io.IOException;

import static java.net.HttpURLConnection.*;


public class PrioritizedHandler extends TaskHandler {
    public PrioritizedHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            if (InMemoryTaskManager.getPrioritizedTasks().isEmpty()) {
                writeResponse(exchange, "Список приоритетных тасок пуст", HTTP_NO_CONTENT);
                return;
            }
            writeResponse(exchange, gson.toJson(InMemoryTaskManager.getPrioritizedTasks()), HTTP_OK);
        } else {
            writeResponse(exchange, "Такого эндпоинта не существует", HTTP_NOT_FOUND);

        }
    }
}
