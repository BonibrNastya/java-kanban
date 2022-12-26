package manager;

import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;
import utils.Managers;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> taskMap;
    protected final Map<Integer, Subtask> subtaskMap;
    protected final Map<Integer, Epic> epicMap;

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    protected int idCounterTask;

    public InMemoryTaskManager() {
        this.taskMap = new HashMap<>();
        this.subtaskMap = new HashMap<>();
        this.epicMap = new HashMap<>();
        this.idCounterTask = 1;
    }

    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime,
                    Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Task::getId);
    Set<Task> taskTreeSet = new TreeSet<>(taskComparator);

    public Set<Task> getPrioritizedTasks() {
        return taskTreeSet;
    }

    @Override
    public void addTask(Task task) {
        task.setId(idCounterTask);
        task.getEndTime();
        taskMap.put(idCounterTask, task);
        taskTreeSet.add(task);
        idCounterTask++;
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(idCounterTask);
        epicMap.put(idCounterTask, epic);
        idCounterTask++;
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(idCounterTask);
        subtask.getEndTime();
        subtaskMap.put(idCounterTask, subtask);
        taskTreeSet.add(subtask);
        idCounterTask++;
        Epic epic = epicMap.get(subtask.getEpicId());
        epic.getSubtaskList().add(subtask);
        epic.updateEpicTime(subtaskMap);
        epic.getEndTime();

    }

    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public Task getTaskById(int id) {

        if (!taskMap.containsKey(id)) {
            throw new RuntimeException("Таска с таким id не найдена. Введен id = " + id + ".");
        }
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        if (!epicMap.containsKey(id)) {
            throw new RuntimeException("Эпик с таким id не найден. Введен id = " + id + ".");
        }
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (!subtaskMap.containsKey(id)) {
            throw new RuntimeException("Сабтаска с таким id не найдена. Введен id = " + id + ".");
        }
        historyManager.add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }

    @Override
    public void updateTask(int id, Task task) {
        if (!taskMap.containsKey(id)) {
            throw new RuntimeException("Таска с id = " + id + " не найдена. Изменение не применилось.");
        }
        task.setId(id);
        task.getEndTime();
        taskMap.put(id, task);
        taskTreeSet.remove(taskMap.get(id));
        taskTreeSet.add(task);

    }

    @Override
    public void updateEpic(int id, Epic epic) {
        if (!epicMap.containsKey(id)) {
            throw new RuntimeException("Эпик с id = " + id + " не найден. Изменение не применилось.");
        }

        Epic targetEpic = epicMap.get(id);
        TaskStatus taskStatus = targetEpic.getStatus();
        epic.setId(id);
        epic.setStatus(taskStatus);
        epicMap.put(id, epic);
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        if (!subtaskMap.containsKey(id)) {
            throw new RuntimeException("Сабтаска с id = " + id + " не найдена. Изменение не применилось.");
        }
        int epicId = subtask.getEpicId();
        if (!epicMap.containsKey(epicId)) {
            throw new RuntimeException("Сабтаска с id = " + id + " не связана с указанным эпиком.");
        }

        subtask.setId(id);
        subtask.getEndTime();
        subtaskMap.put(id, subtask);
        taskTreeSet.remove(subtaskMap.get(id));
        taskTreeSet.add(subtask);
    }

    @Override
    public void clearTasks() {

        for (Map.Entry<Integer, Task> entry : taskMap.entrySet()) {
            taskTreeSet.remove(entry.getValue());
            if (historyManager.getHistory().contains(entry.getKey())) historyManager.remove(entry.getKey());
        }
        taskMap.clear();
    }

    @Override
    public void clearEpics() {
        for (Map.Entry<Integer, Epic> entry : epicMap.entrySet()) {
            taskTreeSet.remove(entry.getValue());
            if (historyManager.getHistory().contains(entry.getKey())) historyManager.remove(entry.getKey());
        }
        epicMap.clear();
    }

    @Override
    public void clearSubtasks() {
        for (Map.Entry<Integer, Subtask> entry : subtaskMap.entrySet()) {
            taskTreeSet.remove(entry.getValue());
            if (historyManager.getHistory().contains(entry.getKey())) historyManager.remove(entry.getKey());
            int epicId = entry.getValue().getEpicId();
            epicMap.get(epicId).setStatus(TaskStatus.NEW);
        }

        subtaskMap.clear();

    }

    @Override
    public void deleteTaskById(int id) {
        if (!taskMap.containsKey(id)) {
            throw new RuntimeException("Таска с id = " + id + " не найдена. Таска не удалилась.");
        }
        taskTreeSet.remove(taskMap.get(id));
        taskMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        if (!epicMap.containsKey(id)) {
            throw new RuntimeException("Эпик с id = " + id + " не найдена. Эпик не удалился.");
        }

        for (Map.Entry<Integer, Subtask> entry : subtaskMap.entrySet()) {
            if (entry.getValue().getEpicId() == id) {
                subtaskMap.remove(entry.getKey());
                taskTreeSet.remove(entry.getValue());
            }
        }
        epicMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (!subtaskMap.containsKey(id)) {
            throw new RuntimeException("Сабтаска с id = " + id + " не найдена. Сабтаска не удалилась.");
        }
        Epic epic = getEpicById(getSubtaskById(id).getEpicId());
        int epicId = epic.getId();
        if (!subtaskMap.containsValue(epicId)) {
            epic.setStatus(TaskStatus.NEW);
        }
        taskTreeSet.remove(subtaskMap.get(id));
        subtaskMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Subtask> getAllSubtasksFromEpic(int id) {
        if (!epicMap.containsKey(id)) {
            throw new RuntimeException("Эпика с id = " + id + " не существует. Невозможно получить список всех сабтасок.");
        }
        if (epicMap.get(id).getSubtaskList().isEmpty()) {
            throw new RuntimeException("У эпика с id = " + id + " нет сабтасок. Невозможно получить список всех сабтасок.");
        }
        List<Subtask> subtaskList = new ArrayList<>();
        for (Map.Entry<Integer, Subtask> entry : subtaskMap.entrySet()) {
            if (id == entry.getValue().getEpicId()) {
                subtaskList.add(entry.getValue());
            }
        }
        return subtaskList;
    }

    @Override
    public void changeSubtaskStatus(Integer id, TaskStatus status) {
        if (!subtaskMap.containsKey(id)) {
            throw new RuntimeException("Сабтаска с id = " + id + " не найдена. Изменить статус сабтаски невозможно.");
        }
        Subtask subtask = subtaskMap.get(id);
        subtask.setStatus(status);
        int epicId = subtask.getEpicId();
        Epic epic = epicMap.get(epicId);
        if (TaskStatus.IN_PROGRESS.equals(status)) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
        if (TaskStatus.DONE.equals(status)) {
            for (Map.Entry<Integer, Subtask> entry : subtaskMap.entrySet()) {
                if ((epicId == entry.getValue().getEpicId()) && !(TaskStatus.DONE == entry.getValue().getStatus())) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                } else epic.setStatus(TaskStatus.DONE);
            }
        }
    }
}

