package ru.yandex.practicum.manager;

import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Subtask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskStatus;
import ru.yandex.practicum.utils.Managers;

import java.io.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {


    private static final Path filePath = Paths.get("src/ru/yandex/practicum/resources/fileBacked.csv");
    private static final String FIRST_LINE_FILE = "id,type,name,status,description,epic\n";
    private static File file;
    private final FileWriter fileWriter = new FileWriter(String.valueOf(filePath), true);

    public FileBackedTasksManager(Path path) throws IOException {
        file = path.toFile();

    }

    public static void main(String[] args) throws IOException {
        FileBackedTasksManager fileManager = Managers.getDefaultFile();

        fileManager.addTask(new Task("Task 1", "Task Dscr 1", TaskStatus.NEW));
        fileManager.addTask(new Task("Task 2", "Task Dscr 2", TaskStatus.NEW));

        fileManager.addEpic(new Epic("Epic 1", "Epic Dscr 1"));

        fileManager.addEpic(new Epic("Epic 2", "Epic Dscr 2"));
        fileManager.addSubtask(new Subtask("Sub 1", "Sub Dscr 1", TaskStatus.NEW, 4));
        fileManager.addSubtask(new Subtask("Sub 2", "Sub Dscr 2", TaskStatus.NEW, 4));

        fileManager.getTaskById(2);
        fileManager.getEpicById(4);
        fileManager.getSubtaskById(5);

        System.out.println(fileManager.getTaskList());
        System.out.println(fileManager.getEpicList());
        System.out.println(fileManager.getSubtaskList());
        System.out.println(fileManager.historyManager.getHistory());

        System.out.println(fileManager.getTaskList());
        System.out.println(fileManager.getEpicList());
        System.out.println(fileManager.getSubtaskList());
        System.out.println(fileManager.historyManager.getHistory());



    }

    private void save() throws IOException {

        StringBuilder resultString = new StringBuilder(FIRST_LINE_FILE);
        List<Task> allTaskList = new ArrayList<>(taskMap.values());
        allTaskList.addAll(epicMap.values());
        allTaskList.addAll(subtaskMap.values());
        allTaskList.stream().sorted(Comparator.comparingInt(Task::getId))
                .forEachOrdered((t) -> resultString.append(t).append("\n"));
        resultString.append("\n");
        if (!historyManager.getHistory().isEmpty()) {
            resultString.append(historyToString(historyManager));
        }
        String result = resultString.toString();
        fileWriter.write(result);
        fileWriter.flush();
    }


    @Override
    public void addTask(Task task) throws IOException {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) throws IOException {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) throws IOException {
        super.addEpic(epic);
        save();
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        manager.getHistory().forEach((t) -> stringBuilder.append(t.getId()).append(','));
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static List<Integer> historyFromString(String history) {
        List<Integer> historyList = new ArrayList<>();
        String[] split = history.split(",");
        for (String s : split) {
            historyList.add(Integer.parseInt(s));
        }
        return historyList;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        List<String[]> tasks = new ArrayList<>();
        String[] history;
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            tasks.add(line.split("\n"));
        }
        line = br.readLine();
        history = line.split("\n");



        if (!tasks.isEmpty()) {
            for (int i = 1; i < tasks.size(); i++) {
                Task task = Task.fromString(String.join("[]", tasks.get(i)));
                fileBackedTasksManager.addTaskToFileBacker(task);
            }
            if (history != null) {
                List<Integer> histories = historyFromString(String.join("[]", history));
                for (Integer integer : histories) {
                    fileBackedTasksManager.addHistoryToFileBacker(integer);
                }
            }
        } else {
            return fileBackedTasksManager;
        }
        return fileBackedTasksManager;
    }

    private void addTaskToFileBacker(Task task) {
        if (task.getClass().equals(Task.class)) {
            taskMap.put(task.getId(), task);
        } else if (task.getClass().equals(Epic.class)){
            epicMap.put(task.getId(),(Epic) task);
        } else {
            subtaskMap.put(task.getId(), (Subtask) task);
        }
    }

    private void addHistoryToFileBacker(Integer value) {
        if (taskMap.containsKey(value)) {
            historyManager.add(taskMap.get(value));
        } else if (epicMap.containsKey(value)) {
            historyManager.add(epicMap.get(value));
        } else {
            historyManager.add(subtaskMap.get(value));
        }
    }
}


