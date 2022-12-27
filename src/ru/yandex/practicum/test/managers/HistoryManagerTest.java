package managers;

import org.junit.jupiter.api.Test;
import manager.HistoryManager;
import manager.TaskManager;
import models.Task;
import models.TaskStatus;
import utils.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TaskManager taskManager = Managers.getDefault();

    Task task = new Task("titleTask", "descrTask", TaskStatus.NEW, Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1));
    Task task2 = new Task("titleTask2", "descrTask2", TaskStatus.NEW, Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1));
    Task task3 = new Task("titleTask3", "descrTask3", TaskStatus.NEW, Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1));


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