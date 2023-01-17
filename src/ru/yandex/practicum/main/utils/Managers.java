package utils;

import manager.http.HttpTaskManager;
import manager.http.KVServer;
import manager.managers.HistoryManager;
import manager.managers.InMemoryHistoryManager;
import manager.managers.InMemoryTaskManager;
import manager.managers.TaskManager;

public class Managers {


    public static TaskManager getInMemoryTaskManager(HistoryManager manager) {
        return new InMemoryTaskManager(manager);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static HttpTaskManager getDefaultHttpManager(HistoryManager historyManager)  {
        return new HttpTaskManager(historyManager,"http://localhost:" + KVServer.PORT);
    }

}


