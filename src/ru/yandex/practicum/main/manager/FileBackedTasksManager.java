package manager;

import exception.ManagerSaveException;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;

import java.io.*;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {


    private static final String FIRST_LINE_FILE = "id,type,name,status,description,duration,startTime,endTime,epic\n";
    private static File file;

    public FileBackedTasksManager(Path path) {
        file = path.toFile();


    }

    public static void main(String[] args) {
        file = new File("src/ru/yandex/practicum/resources/fileBacked.csv");
        FileBackedTasksManager fileManager = new FileBackedTasksManager(file.toPath());

        fileManager.addTask(new Task("titleTask", "descrTask", TaskStatus.NEW, Duration.ofMinutes(5000), LocalDateTime.of(2022,10,1,10,10)));
        //       fileManager.addTask(new Task("Task 2", "Task Dscr 2", TaskStatus.NEW));

        //      fileManager.addEpic(new Epic("Epic 1", "Epic Dscr 1"));
//
//        fileManager.addEpic(new Epic("Epic 2", "Epic Dscr 2"));
        //       fileManager.addSubtask(new Subtask("Sub 1", "Sub Dscr 1", TaskStatus.NEW, 1));
        //       fileManager.addSubtask(new Subtask("Sub 2", "Sub Dscr 2", TaskStatus.NEW, 1));
//
//       fileManager.getTaskById(1);
//        fileManager.getEpicById(4);
//        fileManager.getSubtaskById(5);
        fileManager.save();
        //      FileBackedTasksManager fileManager =  loadFromFile(file);
        //       System.out.println(fileManager.getTaskList());

        //       System.out.println(fileManager.getTaskList());
//        System.out.println(fileManager.getEpicList());
//        System.out.println(fileManager.getSubtaskList());
        //       System.out.println(fileManager.historyManager.getHistory());
//
//        System.out.println(fileManager.getTaskList());
//        System.out.println(fileManager.getEpicList());
//        System.out.println(fileManager.getSubtaskList());
//        System.out.println(fileManager.historyManager.getHistory());


    }

    private void save() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            StringBuilder resultString = new StringBuilder(FIRST_LINE_FILE);
            List<Task> allTaskList = new ArrayList<>();
            allTaskList.addAll(taskMap.values());
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
            fileWriter.close();
        } catch (IOException exp) {
            throw new ManagerSaveException("Ошибка создания нового файла.");
        }
    }


    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    private static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        manager.getHistory().forEach((t) -> stringBuilder.append(t.getId()).append(','));
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private static List<Integer> historyFromString(String history) {
        List<Integer> historyList = new ArrayList<>();
        String[] split = history.split(",");
        for (String s : split) {
            historyList.add(Integer.parseInt(s));
        }
        return historyList;
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file.toPath());
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

            List<String[]> tasks = new ArrayList<>();
            String[] history = null;
            String line;
            while (!(line = br.readLine()).isEmpty()) {
                tasks.add(line.split("\n"));
            }
            if (line == null) {
                line = br.readLine();
                history = line.split("\n");
            }
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
        } finally {
            try {
                br.close();
            } catch (IOException exp) {
                System.out.println(exp.getMessage());
            }
        }
        return fileBackedTasksManager;
    }

    private void addTaskToFileBacker(Task task) {
        if (task.getClass().equals(Task.class)) {
            taskMap.put(task.getId(), task);
        } else if (task.getClass().equals(Epic.class)) {
            epicMap.put(task.getId(), (Epic) task);
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


