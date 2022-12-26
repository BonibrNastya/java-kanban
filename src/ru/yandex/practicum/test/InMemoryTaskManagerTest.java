import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import manager.InMemoryTaskManager;
import models.Epic;
import models.Subtask;
import models.Task;
import models.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        this.manager = new InMemoryTaskManager();
    }

    @Test
    public void getPrioritizedTest() {
        manager.addTask(inputTask);
        manager.addTask(inputTask3);
        manager.addEpic(inputEpic);
        Subtask inputSubtask = new Subtask("title", "descr", TaskStatus.NEW, Duration.ofMinutes(5000), LocalDateTime.of(2020, 10, 15, 10, 10), 3);
        Subtask inputSubtask2 = new Subtask("title", "descr", TaskStatus.NEW, Duration.ofMinutes(4000), LocalDateTime.of(2020, 10, 15, 10, 10), 3);
        manager.addSubtask(inputSubtask);
        manager.addSubtask(inputSubtask2);

        Set<Task> actualPrioritizedList = manager.getPrioritizedTasks();

        assertNotNull(actualPrioritizedList, "Отсортированный список тасок пуст.");
    }

    @Test
    public void shouldDeleteSubtaskInTreeWhenDeleteEpicById() {

        manager.addEpic(inputEpic);
        manager.addEpic(inputEpic2);
        int epicId = inputEpic.getId();
        manager.getEpicById(epicId);
        manager.getEpicById(2);

        Subtask inputSubtask = new Subtask("title", "descr", TaskStatus.NEW,Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1), epicId);

        manager.addSubtask(inputSubtask);

        manager.deleteEpicById(epicId);
        List<Epic> actualEpicList = manager.getEpicList();
        List<Epic> expectedEpicList = new ArrayList<>();
        expectedEpicList.add(inputEpic2);

        assertEquals(expectedEpicList, actualEpicList, "Удаление эпика по id некорректно.");

        assertNotNull(manager.getSubtaskList(), "Сабтаска с удаленным эпиком не удалилась.");

        assertNotNull(manager.getPrioritizedTasks(), "Не удалилась сабтаска после удаления связанного эпика.");
    }
}