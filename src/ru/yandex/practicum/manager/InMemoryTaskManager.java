package ru.yandex.practicum.manager;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Subtask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> taskMap;
    private final Map<Integer, Subtask> subtaskMap;
    private final Map<Integer, Epic> epicMap;


    private int idCounterTask;
    private int idCounterSubtask;
    private int getIdCounterEpic;

    public InMemoryTaskManager() {
        this.taskMap = new HashMap<>();
        this.subtaskMap = new HashMap<>();
        this.epicMap = new HashMap<>();


        this.idCounterTask = 1;
        this.idCounterSubtask = 1;
        this.getIdCounterEpic = 1;
    }
    public HistoryManager getDefaultHistory(){
        return getDefaultHistory();
    }

    @Override
    public Task getTaskById(int id) {
        HistoryManager taskHistory = new InMemoryHistoryManager();
        taskHistory.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        HistoryManager subtaskHistory = new InMemoryHistoryManager();
        subtaskHistory.add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        HistoryManager epicHistory = new InMemoryHistoryManager();
        epicHistory.add(epicMap.get(id));
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
    public void clearTasks() {
        taskMap.clear();
        idCounterTask = 1;
    }

    @Override
    public void updateTask(Task task) {
        taskMap.remove(task.getId());
        taskMap.put(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {
        taskMap.remove(id);
    }

    @Override
    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public void clearSubtasks() {
        subtaskMap.clear();
        idCounterSubtask = 1;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtaskMap.remove(subtask.getId());
        subtaskMap.put(subtask.getId(), subtask);
    }

    @Override
    public void deleteSubtaskById(int id) {
        subtaskMap.remove(id);
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public void clearEpics() {
        epicMap.clear();
        getIdCounterEpic = 1;
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
    }

    @Override
    public void addTask(Task task) {
        task.setId(idCounterTask);
        taskMap.put(idCounterTask, task);
        idCounterTask++;
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(idCounterSubtask);
        subtaskMap.put(idCounterSubtask, subtask);
        idCounterSubtask++;
        Epic epic = epicMap.get(subtask.getEpicId());
        epic.getSubtaskList().add(subtask);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(getIdCounterEpic);
        epicMap.put(getIdCounterEpic, epic);
        getIdCounterEpic++;
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

