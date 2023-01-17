package manager.http;

import com.sun.net.httpserver.HttpServer;
import manager.http.handlers.*;
import manager.managers.HistoryManager;
import manager.managers.TaskManager;
import utils.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private static final int PORT = 8080;

    public HttpTaskServer() throws IOException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefaultHttpManager(historyManager);
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler(taskManager));
        httpServer.createContext("/tasks/subtask", new SubHandler(taskManager));
        httpServer.createContext("/tasks/epic", new EpicHandler(taskManager));
        httpServer.createContext("/tasks/subtask/epic", new SubByEpicHandler(taskManager));
        httpServer.createContext("/tasks/history", new HistoryHandler(taskManager));
        httpServer.createContext("/tasks", new PrioritizedHandler(taskManager));

        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");

    }
}

