package ru.yandex.practicum.manager;

import ru.yandex.practicum.exception.ManagerSaveException;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Subtask;
import ru.yandex.practicum.models.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final Path file;
    private final FileWriter fileWriter = new FileWriter("src/ru/yandex/practicum/resources/autoStorage.csv");
    private static final String FIRST_LINE_FILE = "id,type,name,status,description,epic\n";


    public FileBackedTasksManager(Path path) throws IOException {
        this.file = path.getFileName();
    }

    private void save() throws IOException {
        StringBuilder resultString = new StringBuilder(FIRST_LINE_FILE);
        List<Task> allTaskList = new ArrayList<>(taskMap.values());
        allTaskList.addAll(epicMap.values());
        allTaskList.addAll(subtaskMap.values());
        allTaskList.stream().sorted(Comparator.comparingInt(Task::getId)).forEachOrdered((t) -> resultString.append(t).append("\n"));
        resultString.append("\n");
        resultString.append(historyToString(historyManager));
        try {
            fileWriter.write(resultString.toString());
        } catch (IOException exp) {
            throw new ManagerSaveException();
        }
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
        stringBuilder.deleteCharAt(stringBuilder.length());
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
        String[] values = null;
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            values = line.split("\n");
        }
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(Path.of("src/ru/yandex/practicum/resources/autoStorage.csv"));
        if (values != null) {
            for (int i = 1; i < values.length - 2; i++) {
                Task task = Task.fromString(values[i]);
                assert task != null;
                if (task.getClass().equals(Task.class)) {
                    fileBackedTasksManager.taskMap.put(task.getId(), task);
                }
                if (task.getClass().equals(Subtask.class)) {
                    fileBackedTasksManager.subtaskMap.put(task.getId(), (Subtask) task);
                }
                if (task.getClass().equals(Epic.class)) {
                    fileBackedTasksManager.epicMap.put(task.getId(), (Epic) task);
                }
            }
        }
        assert values != null;
        List<Integer> historyIds = historyFromString(values[values.length - 1]);
        for (Integer integer : historyIds) {
            if (fileBackedTasksManager.taskMap.containsKey(integer)) {
                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.taskMap.get(integer));
            }
            if (fileBackedTasksManager.epicMap.containsKey(integer)) {
                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.epicMap.get(integer));
            }
            if (fileBackedTasksManager.subtaskMap.containsKey(integer)) {
                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.subtaskMap.get(integer));
            }
        }
        return fileBackedTasksManager;
    }
}
