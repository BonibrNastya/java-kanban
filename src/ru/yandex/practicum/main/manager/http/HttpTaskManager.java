package manager.http;

import com.google.gson.*;
import manager.managers.FileBackedTasksManager;
import manager.managers.HistoryManager;
import models.Epic;
import models.Subtask;
import models.Task;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .create();


    public HttpTaskManager(HistoryManager historyManager, String path) {
        super(historyManager);
        client = new KVTaskClient(path);

        JsonElement tasks = JsonParser.parseString(client.load("tasks"));
        if (!tasks.isJsonNull()) {
            JsonArray jsonArray = tasks.getAsJsonArray();
            for (JsonElement jsonTask : jsonArray) {
                Task task = gson.fromJson(jsonTask, Task.class);
                taskMap.put(task.getId(), task);
            }
        }

        JsonElement epics = JsonParser.parseString(client.load("epics"));
        if (!epics.isJsonNull()) {
            JsonArray jsonArray = epics.getAsJsonArray();
            for (JsonElement jsonTask : jsonArray) {
                Epic task = gson.fromJson(jsonTask, Epic.class);
                epicMap.put(task.getId(), task);
            }
        }

        JsonElement subs = JsonParser.parseString(client.load("subtasks"));
        if (!subs.isJsonNull()) {
            JsonArray jsonArray = subs.getAsJsonArray();
            for (JsonElement jsonTask : jsonArray) {
                Subtask task = gson.fromJson(jsonTask, Subtask.class);
                subtaskMap.put(task.getId(), task);
            }
        }

        JsonElement histories = JsonParser.parseString(client.load("history"));
        if (!histories.isJsonNull()) {
            JsonArray jsonArray = histories.getAsJsonArray();
            for (JsonElement json : jsonArray) {
                int id = json.getAsInt();
                if (taskMap.containsKey(id)) {
                    historyManager.add(taskMap.get(id));
                } else if (epicMap.containsKey(id)) {
                    historyManager.add(epicMap.get(id));
                } else if (subtaskMap.containsKey(id)) {
                    historyManager.add(subtaskMap.get(id));
                }

            }
        }

    }

    @Override
    public void save() {
        client.put("tasks", gson.toJson(taskMap.values()));
        client.put("epics", gson.toJson(epicMap.values()));
        client.put("subtasks", gson.toJson(subtaskMap.values()));
        client.put("history", gson.toJson(historyManager.getHistory()
                .stream().map(Task::getId).collect(Collectors.toList())));
    }
}
