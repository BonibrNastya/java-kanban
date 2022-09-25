package ru.yandex.practicum.manager;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Subtask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskStatus;

import java.util.List;

public interface TaskManager {

    List<Task> getTaskList();
    void clearTasks();
    Task getTaskById(int id);
    void updateTask(Task task);
    void deleteTaskById(int id);
    List<Subtask> getSubtaskList();
    void clearSubtasks();
    Subtask getSubtaskById(int id);
    void updateSubtask(Subtask subtask);
    void deleteSubtaskById(int id);
    List<Epic> getEpicList();
    void clearEpics();
    Epic getEpicById(int id);
    void updateEpic(int id, Epic epic);
    void deleteEpicById(int id);
    List<Subtask> getAllSubtasksFromEpic(int id);
    void addTask(Task task);
    void addSubtask(Subtask subtask);
    void addEpic(Epic epic);
    void changeSubtaskStatus(Integer id, TaskStatus status);
}
