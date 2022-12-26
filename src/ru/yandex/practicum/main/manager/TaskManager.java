package manager;

import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;

import java.util.List;

public interface TaskManager {

    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    List<Task> getTaskList();

    List<Epic> getEpicList();

    List<Subtask> getSubtaskList();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void updateTask(int id, Task task);

    void updateEpic(int id, Epic epic);

    void updateSubtask(int id, Subtask subtask);

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    void changeSubtaskStatus(Integer id, TaskStatus status);

    List<Subtask> getAllSubtasksFromEpic(int id);

}
