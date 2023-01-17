
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.http.KVServer;
import manager.http.LocalDateAdapter;
import manager.managers.HistoryManager;
import manager.managers.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import models.enums.TaskStatus;
import utils.Managers;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        KVServer server;
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                    .create();

            server = new KVServer();
            server.start();
            HistoryManager historyManager = Managers.getDefaultHistory();
            TaskManager httpTaskManager = Managers.getDefaultHttpManager(historyManager);

            Task task1 = new Task("titTask", "desTask", TaskStatus.NEW, 100L, LocalDateTime.of(2020, 10, 10, 10, 10));
            httpTaskManager.addTask(task1);
            Task task2 = new Task("titTask2", "desTask2", TaskStatus.NEW, 100L, LocalDateTime.of(2020, 10, 10, 10, 10));
            httpTaskManager.addTask(task2);

            Epic epic1 = new Epic("titEpic", "desEpic", 100L, LocalDateTime.of(2022, 10, 10, 10, 10));
            httpTaskManager.addEpic(epic1);

            Subtask subtask1 = new Subtask("titSub", "desSub", TaskStatus.NEW, 200L, LocalDateTime.of(2022, 10, 11, 10, 10), epic1.getId());
            httpTaskManager.addSubtask(subtask1);


            System.out.println(gson.toJson(httpTaskManager.getTaskById(task1.getId())));
            System.out.println(gson.toJson(httpTaskManager.getEpicById(epic1.getId())));
            System.out.println(gson.toJson(httpTaskManager.getSubtaskById(subtask1.getId())));

            System.out.println(gson.toJson(httpTaskManager.getTaskList()));
            System.out.println(gson.toJson(httpTaskManager.getEpicList()));
            System.out.println(gson.toJson(httpTaskManager.getSubtaskList()));
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}