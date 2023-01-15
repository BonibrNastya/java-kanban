package utils;

import manager.*;

import java.io.IOException;

public class Managers {

    //    public static TaskManager getDefault() {
//        return new InMemoryTaskManager();
//    }
    public static TaskManager getInMemoryTaskManager(HistoryManager manager) {
        return new InMemoryTaskManager(manager);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

//    public static FileBackedTasksManager getDefaultFileManager(){
//        return new FileBackedTasksManager(Path.of("src/ru/yandex/practicum/main/resources/fileHttp.csv"));
//    }

    public static HttpTaskManager getDefaultHttpManager(HistoryManager historyManager) throws IOException, InterruptedException {
        return new HttpTaskManager(historyManager,"http://localhost:" + KVServer.PORT);
    }

}


