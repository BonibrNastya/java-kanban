package models;

import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import manager.TaskManager;
import utils.Managers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private static TaskManager taskManager;
    private static Epic epic;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getInMemoryTaskManager(new InMemoryHistoryManager());
        epic = new Epic("title1", "descr1");
        taskManager.addEpic(epic);

    }

    @Test
    public void addNewEpic() {
        final int epicId = epic.getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0));

    }

    @Test
    public void shouldBeNewIfThereIsNoSubtask() {

        final TaskStatus status = epic.getStatus();

        assertEquals(TaskStatus.NEW, status, "Неверный статус эпика.");

    }

    @Test
    public void shouldBeNewIfSubtasksWithStatusNew() {

        int epicId = epic.getId();
        Subtask subtask = new Subtask("titleSub", "descrSub", TaskStatus.NEW, 5000L, LocalDateTime.of(2000, 1, 1, 1, 1), epicId);
        Subtask subtask2 = new Subtask("titleSub", "descrSub", TaskStatus.NEW, 5000L, LocalDateTime.of(2000, 1, 1, 1, 1), epicId);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        TaskStatus epicStatus = epic.getStatus();

        assertEquals(epicStatus, TaskStatus.NEW, "Неверный статус эпика с новыми сабтасками");

    }

    @Test
    public void shouldBeDoneIfSubtasksWithStatusDone() {

        int epicId = epic.getId();
        Subtask subtask = new Subtask("titleSub", "descrSub", TaskStatus.NEW, 5000L, LocalDateTime.of(2000, 1, 1, 1, 1), epicId);
        Subtask subtask2 = new Subtask("titleSub", "descrSub", TaskStatus.NEW, 5000L, LocalDateTime.of(2000, 1, 1, 1, 1), epicId);

        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        int subtaskId = subtask.getId();
        int subtaskId2 = subtask2.getId();

        taskManager.changeSubtaskStatus(subtaskId, TaskStatus.DONE);
        taskManager.changeSubtaskStatus(subtaskId2, TaskStatus.DONE);

        TaskStatus epicStatus = epic.getStatus();

        assertEquals(epicStatus, TaskStatus.DONE, "Неверный статус эпика с выполненными сабтасками.");

    }

    @Test
    public void shouldBeInProgressIfSubtasksWithStatusNewAndDone() {
        int epicId = epic.getId();
        Subtask subtask = new Subtask("titleSub", "descrSub", TaskStatus.NEW, 5000L, LocalDateTime.of(2000, 1, 1, 1, 1), epicId);
        Subtask subtask2 = new Subtask("titleSub", "descrSub", TaskStatus.NEW, 5000L, LocalDateTime.of(2000, 1, 1, 1, 1), epicId);

        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        int subtaskId2 = subtask2.getId();

        taskManager.changeSubtaskStatus(subtaskId2, TaskStatus.IN_PROGRESS);

        TaskStatus epicStatus = epic.getStatus();

        assertEquals(epicStatus, TaskStatus.IN_PROGRESS, "Неверный статус эпика с разными статусами сабтасок.");
    }

    @Test
    public void shouldBeInProgressIfSubtasksWithStatusInProgress() {
        int epicId = epic.getId();
        Subtask subtask = new Subtask("titleSub", "descrSub", TaskStatus.NEW, 5000L, LocalDateTime.of(2000, 1, 1, 1, 1), epicId);
        Subtask subtask2 = new Subtask("titleSub", "descrSub", TaskStatus.NEW, 5000L, LocalDateTime.of(2000, 1, 1, 1, 1), epicId);

        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        int subtaskId = subtask.getId();
        int subtaskId2 = subtask2.getId();

        taskManager.changeSubtaskStatus(subtaskId, TaskStatus.IN_PROGRESS);
        taskManager.changeSubtaskStatus(subtaskId2, TaskStatus.IN_PROGRESS);

        TaskStatus epicStatus = epic.getStatus();

        assertEquals(epicStatus, TaskStatus.IN_PROGRESS, "Неверный статус эпика с сабтасками со статусом в процессе.");

    }

}