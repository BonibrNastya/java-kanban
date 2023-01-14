
import com.google.gson.Gson;
import manager.HistoryManager;
import manager.KVServer;
import manager.TaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;
import utils.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        // Спринт 8
        KVServer server;
        try {
            Gson gson = new Gson();

            server = new KVServer();
            server.start();
            TaskManager httpTaskManager = Managers.getDefaultHttpManager();

            Task task1 = new Task(
                    "Разработать лифт до луны", "Космолифт",
                    TaskStatus.NEW,
                    Duration.ofMinutes(100),
                    LocalDateTime.of(2020,10,10,10,10)
            );
            httpTaskManager.addTask(task1);

            Epic epic1 = new Epic(
                    "Посадить дерево",
                    "Дерево"
            );
            httpTaskManager.addEpic(epic1);

            Subtask subtask1 = new Subtask(
                    "Купить семена",
                    "Семена",
                    TaskStatus.NEW,
                    Duration.ofMinutes(200),
                    LocalDateTime.of(2022,10,10,10,10),
                    epic1.getId()
            );
            httpTaskManager.addSubtask(subtask1);


            httpTaskManager.getTaskById(task1.getId());
            httpTaskManager.getEpicById(epic1.getId());
            httpTaskManager.getSubtaskById(subtask1.getId());

            System.out.println("Печать всех задач");
            System.out.println(gson.toJson(httpTaskManager.getTaskList()));
            System.out.println("Печать всех эпиков");
            System.out.println(gson.toJson(httpTaskManager.getEpicList()));
            System.out.println("Печать всех подзадач");
            System.out.println(gson.toJson(httpTaskManager.getSubtaskList()));
            System.out.println("Загруженный менеджер");
            System.out.println(httpTaskManager);
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}