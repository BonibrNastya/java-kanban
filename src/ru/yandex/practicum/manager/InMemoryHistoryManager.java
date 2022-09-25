package ru.yandex.practicum.manager;

import ru.yandex.practicum.models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> history;

    public InMemoryHistoryManager() {
        this.history = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        if (history.size() < 11) {
            for (int i = 1; i < history.size(); i++) {
                historyList.add(history.get(i));
            }
        } else {
            history.remove(0);
        }
        return historyList;
    }
}
