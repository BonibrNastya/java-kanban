package ru.yandex.practicum.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskStatus;
import ru.yandex.practicum.utils.Managers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistory();
    Task task = new Task("titleTask", "descrTask", TaskStatus.NEW);
    Task task2 = new Task("titleTask2", "descrTask2", TaskStatus.NEW);
    Task task3 = new Task("titleTask3", "descrTask3", TaskStatus.NEW);
    TaskManager taskManager = Managers.getDefault();


    @Test
    void add() {
        taskManager.addTask(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");

    }

    @Test
    void shouldReturnThreeTaskWhenAddTaskTask2Task3Task() {
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "Неверное добавление истории при дублировании тасок.");

    }

    @Test
    void removeSingleHistory() {
        taskManager.addTask(task);
        historyManager.add(task);
        historyManager.remove(1);
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "Неправильное удаление истории.");
    }

    @Test
    void removeMiddleHistory() {
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(2);
        List<Task> actualHistoryListAfterRemoveMiddleHistory = historyManager.getHistory();
        List<Task> expectedHistoryListAfterRemove = new ArrayList<>();
        expectedHistoryListAfterRemove.add(task);
        expectedHistoryListAfterRemove.add(task3);
        assertEquals(expectedHistoryListAfterRemove,
                actualHistoryListAfterRemoveMiddleHistory,
                "Неправильное удаление истории из середины списка.");
    }

    @Test
    void removeLastHistory() {
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(3);
        List<Task> actualHistoryList = historyManager.getHistory();
        List<Task> expectedHistoryList = new ArrayList<>();
        expectedHistoryList.add(task);
        expectedHistoryList.add(task2);
        assertEquals(expectedHistoryList, actualHistoryList, "Неправильное удаление последней истории.");
    }

    @Test
    void shouldReturnHistoryNotFoundException() {
        RuntimeException exp = assertThrows(RuntimeException.class, () -> historyManager.remove(4));
        assertEquals("История с введенным id не найдена.", exp.getMessage());
    }

}