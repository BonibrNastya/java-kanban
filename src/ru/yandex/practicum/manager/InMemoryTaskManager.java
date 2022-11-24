package ru.yandex.practicum.manager;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Subtask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskStatus;
import ru.yandex.practicum.utils.Managers;

import java.io.IOException;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> taskMap;
    protected final Map<Integer, Subtask> subtaskMap;
    protected final Map<Integer, Epic> epicMap;
    protected HistoryManager historyManager = Managers.getDefaultHistory();


    private int idCounterTask;

    public InMemoryTaskManager() {
        this.taskMap = new HashMap<>();
        this.subtaskMap = new HashMap<>();
        this.epicMap = new HashMap<>();


        this.idCounterTask = 1;
    }

    @Override
    public void clearTasks() {

        for (Map.Entry<Integer, Task> entry : taskMap.entrySet()) {
            historyManager.remove(entry.getKey());
        }
        taskMap.clear();
    }


    @Override
    public Task getTaskById(int id) {
        if (!taskMap.containsKey(id)){
            System.out.println("Такого id нет");
            return null;
        }
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (!subtaskMap.containsKey(id)){
            System.out.println("Такого id нет");
            return null;
        }
        historyManager.add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        if (!epicMap.containsKey(id)){
            System.out.println("Такого id нет");
            return null;
        }
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public List<Subtask> getAllSubtasksFromEpic(int id) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Map.Entry<Integer, Subtask> entry : subtaskMap.entrySet()) {
            if (id == entry.getValue().getEpicId()) {
                subtaskList.add(entry.getValue());
            }
        }
        return subtaskList;
    }

    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public void updateTask(Task task) {
        taskMap.remove(task.getId());
        taskMap.put(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {
        taskMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public void clearSubtasks() {
        for (Map.Entry<Integer, Subtask> entry : subtaskMap.entrySet()) {
            historyManager.remove(entry.getKey());
        }
        subtaskMap.clear();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskMap.remove(subtask.getId());
        subtaskMap.put(subtask.getId(), subtask);
    }

    @Override
    public void deleteSubtaskById(int id) {
        subtaskMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public void clearEpics() {
        for (Map.Entry<Integer, Epic> entry : epicMap.entrySet()) {
            historyManager.remove(entry.getKey());
        }
        epicMap.clear();
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        Epic targetEpic = epicMap.get(id);
        TaskStatus taskStatus = targetEpic.getStatus();
        epicMap.remove(id);
        epic.setStatus(taskStatus);
        epicMap.put(id, epic);
    }

    @Override
    public void deleteEpicById(int id) {
        epicMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void addTask(Task task) throws IOException {
        task.setId(idCounterTask);
        taskMap.put(idCounterTask, task);
        idCounterTask++;
    }

    @Override
    public void addSubtask(Subtask subtask) throws IOException {
        subtask.setId(idCounterTask);
        subtaskMap.put(idCounterTask, subtask);
        idCounterTask++;
        Epic epic = epicMap.get(subtask.getEpicId());
        epic.getSubtaskList().add(subtask);
    }

    @Override
    public void addEpic(Epic epic) throws IOException {
        epic.setId(idCounterTask);
        epicMap.put(idCounterTask, epic);
        idCounterTask++;
    }

    @Override
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

