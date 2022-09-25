package ru.yandex.practicum.utils;

import ru.yandex.practicum.manager.HistoryManager;
import ru.yandex.practicum.manager.InMemoryHistoryManager;
import ru.yandex.practicum.manager.InMemoryTaskManager;
import ru.yandex.practicum.manager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

