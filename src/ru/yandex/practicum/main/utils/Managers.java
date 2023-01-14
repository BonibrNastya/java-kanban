package utils;

import manager.*;

import java.io.IOException;
import java.nio.file.Path;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

//    public static FileBackedTasksManager getDefaultFileManager(){
//        return new FileBackedTasksManager(Path.of("src/ru/yandex/practicum/main/resources/fileHttp.csv"));
//    }

    public static HttpTaskManager getDefaultHttpManager() throws IOException, InterruptedException {
        return new HttpTaskManager("http://localhost:" + KVServer.PORT);
    }

}


