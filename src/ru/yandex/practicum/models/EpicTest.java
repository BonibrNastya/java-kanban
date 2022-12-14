package ru.yandex.practicum.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.utils.Managers;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private static TaskManager taskManager;
    private static Epic epic;

    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = Managers.getDefault();
        epic = new Epic("title1", "descr1");
        taskManager.addEpic(epic);

    }
    //подготовка - определение входных параметров и предусловий

    //исполнение - определение процедуры тестирования

    //проверка - сравнение ожидаемого результата с полученным


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
    public void shouldBeNewIfSubtasksWithStatusNew() throws IOException {

        int epicId = epic.getId();
        Subtask subtask = new Subtask("titleSub", "descrSub", TaskStatus.NEW, epicId);
        Subtask subtask2 = new Subtask("titleSub", "descrSub", TaskStatus.NEW, epicId);
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);
        TaskStatus epicStatus = epic.getStatus();

        assertEquals(epicStatus, TaskStatus.NEW, "Неверный статус эпика с новыми сабтасками");

    }

    @Test
    public void shouldBeDoneIfSubtasksWithStatusDone() throws IOException {

        int epicId = epic.getId();
        Subtask subtask = new Subtask("titleSub", "descrSub", TaskStatus.NEW, epicId);
        Subtask subtask2 = new Subtask("titleSub", "descrSub", TaskStatus.NEW, epicId);

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
    public void shouldBeInProgressIfSubtasksWithStatusNewAndDone() throws IOException {
        int epicId = epic.getId();
        Subtask subtask = new Subtask("titleSub", "descrSub", TaskStatus.NEW, epicId);
        Subtask subtask2 = new Subtask("titleSub", "descrSub", TaskStatus.NEW, epicId);

        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        int subtaskId2 = subtask2.getId();

        taskManager.changeSubtaskStatus(subtaskId2, TaskStatus.DONE);

        TaskStatus epicStatus = epic.getStatus();

        assertEquals(epicStatus, TaskStatus.IN_PROGRESS, "Неверный статус эпика с разными статусами сабтасок.");
    }

    @Test
    public void shouldBeInProgressIfSubtasksWithStatusInProgress() throws IOException {
        int epicId = epic.getId();
        Subtask subtask = new Subtask("titleSub", "descrSub", TaskStatus.NEW, epicId);
        Subtask subtask2 = new Subtask("titleSub", "descrSub", TaskStatus.NEW, epicId);

        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask2);

        int subtaskId = subtask.getId();
        int subtaskId2 = subtask2.getId();

        taskManager.changeSubtaskStatus(subtaskId, TaskStatus.IN_PROGRESS);
        taskManager.changeSubtaskStatus(subtaskId2, TaskStatus.IN_PROGRESS);

        TaskStatus epicStatus = epic.getStatus();

        assertEquals(epicStatus, TaskStatus.IN_PROGRESS, "Неверный статус эпика с сабтасками в процессе.");

    }

}