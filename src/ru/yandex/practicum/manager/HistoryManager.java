package ru.yandex.practicum.manager;

import ru.yandex.practicum.models.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);

    List<Task> getHistory();

}
