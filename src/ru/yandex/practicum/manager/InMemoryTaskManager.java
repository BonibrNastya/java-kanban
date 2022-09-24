package ru.yandex.practicum.manager;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Subtask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    Map<Integer, Task> taskMap = new HashMap<>();
    Map<Integer, Subtask> subtaskMap = new HashMap<>();
    Map<Integer, Epic> epicMap = new HashMap<>();
    Deque<Task> history = new LinkedList<>();

    int idCounterTask = 1;
    int idCounterSubtask = 1;
    int getIdCounterEpic = 1;

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        if (history.size()<11){
            for (int i = 1; i <history.size(); i++) {
                historyList.add(history.getLast());
            }
        } else{
            history.removeFirst();
        }
        return historyList;
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
    public Task getTaskById(int id) {
        history.addFirst(taskMap.get(id));
        return taskMap.get(id);
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
    public Subtask getSubtaskById(int id) {
        history.addFirst(subtaskMap.get(id));
        return subtaskMap.get(id);
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
    public Epic getEpicById(int id) {
        history.addFirst(epicMap.get(id));
        return epicMap.get(id);
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

