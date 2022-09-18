import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    static Map<Integer, Task> taskMap = new HashMap<>();
    static Map<Integer, Subtask> subtaskMap = new HashMap<>();
    static Map<Integer, Epic> epicMap = new HashMap<>();

    static int idCounterTask = 1;
    static int idCounterSubtask = 1;
    static int getIdCounterEpic = 1;

    public static List<Task> getTaskList() {
        return new ArrayList<>(taskMap.values());
    }

    public static void clearTasks() {
        taskMap.clear();
        idCounterTask = 1;
    }

    public static Task getTaskById(int id) {
        return taskMap.get(id);
    }

    public static void updateTask(Task task) {
        taskMap.remove(task.getId());
        taskMap.put(task.getId(), task);
    }

    public static void deleteTaskById(int id) {
        taskMap.remove(id);
    }


    public static List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskMap.values());
    }

    public static void clearSubtasks() {
        subtaskMap.clear();
        idCounterSubtask = 1;
    }

    public static Subtask getSubtaskById(int id) {
        return subtaskMap.get(id);
    }

    public static void updateSubtask(Subtask subtask) {
        subtaskMap.remove(subtask.getId());
        subtaskMap.put(subtask.getId(), subtask);
    }

    public static void deleteSubtaskById(int id) {
        subtaskMap.remove(id);
    }

    public static List<Epic> getEpicList() {
        return new ArrayList<>(epicMap.values());
    }

    public static void clearEpics() {
        epicMap.clear();
        getIdCounterEpic = 1;
    }

    public static Epic getEpicById(int id) {
        return epicMap.get(id);
    }

    public static void updateEpic(int id, Epic epic) {
        Epic targetEpic = epicMap.get(id);
        TaskStatus taskStatus = targetEpic.getStatus();
        epicMap.remove(id);
        epic.setStatus(taskStatus);
        epicMap.put(id, epic);
    }

    public static void deleteEpicById(int id) {
        epicMap.remove(id);
    }

    public static List<Subtask> getAllSubtasksFromEpic(int id) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Map.Entry<Integer, Subtask> entry : subtaskMap.entrySet()) {
            if (id == entry.getValue().getEpicId()) {
                subtaskList.add(entry.getValue());
            }
        }
        return subtaskList;
    }

    public static void addTask(Task task) {
        task.setId(idCounterTask);
        taskMap.put(idCounterTask, task);
        idCounterTask++;
    }

    public static void addSubtask(Subtask subtask) {
        subtask.setId(idCounterSubtask);
        subtaskMap.put(idCounterSubtask, subtask);
        idCounterSubtask++;
        Epic epic = epicMap.get(subtask.getEpicId());
        epic.getSubtaskList().add(subtask);
    }

    public static void addEpic(Epic epic) {
        epic.setId(getIdCounterEpic);
        epicMap.put(getIdCounterEpic, epic);
        getIdCounterEpic++;
    }

    public static void changeSubtaskStatus(Integer id, TaskStatus status) {
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
