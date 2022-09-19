package ru.yandex.practicum.manager;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Subtask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    Map<Integer, Task> taskMap = new HashMap<>();
    Map<Integer, Subtask> subtaskMap = new HashMap<>();
    Map<Integer, Epic> epicMap = new HashMap<>();

    int idCounterTask = 1;
    int idCounterSubtask = 1;
    int getIdCounterEpic = 1;

    public List<Task> getTaskList() {
        return new ArrayList<>(taskMap.values());
    }

    public void clearTasks() {
        taskMap.clear();
        idCounterTask = 1;
    }

    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public void updateTask(Task task) {
        taskMap.remove(task.getId());
        taskMap.put(task.getId(), task);
    }

    public void deleteTaskById(int id) {
        taskMap.remove(id);
    }


    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskMap.values());
    }

    public void clearSubtasks() {
        subtaskMap.clear();
        idCounterSubtask = 1;
    }

    public Subtask getSubtaskById(int id) {
        return subtaskMap.get(id);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskMap.remove(subtask.getId());
        subtaskMap.put(subtask.getId(), subtask);
    }

    public void deleteSubtaskById(int id) {
        subtaskMap.remove(id);
    }

    public List<Epic> getEpicList() {
        return new ArrayList<>(epicMap.values());
    }

    public void clearEpics() {
        epicMap.clear();
        getIdCounterEpic = 1;
    }

    public Epic getEpicById(int id) {
        return epicMap.get(id);
    }

    public void updateEpic(int id, Epic epic) {
        Epic targetEpic = epicMap.get(id);
        TaskStatus taskStatus = targetEpic.getStatus();
        epicMap.remove(id);
        epic.setStatus(taskStatus);
        epicMap.put(id, epic);
    }

    public void deleteEpicById(int id) {
        epicMap.remove(id);
    }

    public List<Subtask> getAllSubtasksFromEpic(int id) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Map.Entry<Integer, Subtask> entry : subtaskMap.entrySet()) {
            if (id == entry.getValue().getEpicId()) {
                subtaskList.add(entry.getValue());
            }
        }
        return subtaskList;
    }

    public void addTask(Task task) {
        task.setId(idCounterTask);
        taskMap.put(idCounterTask, task);
        idCounterTask++;
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(idCounterSubtask);
        subtaskMap.put(idCounterSubtask, subtask);
        idCounterSubtask++;
        Epic epic = epicMap.get(subtask.getEpicId());
        epic.getSubtaskList().add(subtask);
    }

    public void addEpic(Epic epic) {
        epic.setId(getIdCounterEpic);
        epicMap.put(getIdCounterEpic, epic);
        getIdCounterEpic++;
    }

    public void changeSubtaskStatus(Integer id, TaskStatus status) {
        Subtask subtask = subtaskMap.get(id);
        subtask.setStatus(status);
        int epicId = subtask.getEpicId();
        if (TaskStatus.IN_PROGRESS.equals(status)) {
            Epic epic = epicMap.get(epicId);
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
        for (Map.Entry<Integer, Subtask> entry : subtaskMap.entrySet()) {
            if ((epicId == entry.getValue().getEpicId()) && !(TaskStatus.DONE == entry.getValue().getStatus())) {
                return;
            }
        }
        Epic epic = epicMap.get(epicId);
        epic.setStatus(TaskStatus.DONE);
    }
}
