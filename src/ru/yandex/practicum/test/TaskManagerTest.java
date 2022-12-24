import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.TaskManager;
import ru.yandex.practicum.models.Epic;
import ru.yandex.practicum.models.Subtask;
import ru.yandex.practicum.models.Task;
import ru.yandex.practicum.models.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    T manager;

    Task inputTask = new Task("titleTask", "descrTask", TaskStatus.NEW, Duration.ofMinutes(5000), LocalDateTime.of(2022,10,1,10,10));
    Task inputTask2 = new Task("titleTask2", "descrTask2", TaskStatus.NEW, Duration.ofMinutes(6000), LocalDateTime.of(2020,10,20,20,20));
    Task inputTask3 = new Task("titleTask3", "descrTask2", TaskStatus.NEW, Duration.ofMinutes(4000), LocalDateTime.of(2010,5,5,5,5));

    Epic inputEpic = new Epic("titleEpic", "descrEpic");
    Epic inputEpic2 = new Epic("titleEpic2", "descrEpic2");
    Epic inputEpic3 = new Epic("titleEpic3", "descrEpic3");


    @Test
    public void shouldReturnTheSameTaskAfterAddTask() {

        manager.addTask(inputTask);
        int taskId = inputTask.getId();

        Task outputTask = manager.getTaskById(taskId);
        assertEquals(inputTask, outputTask, "Записанная таска не соответствует полученной.");
    }


    @Test
    public void shouldReturnTheSameEpicAfterAddEpic() {

        manager.addEpic(inputEpic);
        int epicId = inputEpic.getId();

        Epic outputEpic = manager.getEpicById(epicId);
        assertEquals(inputEpic, outputEpic, "Записанный эпик не соответствует полученному.");

    }

    @Test
    public void shouldReturnTheSameSubtaskAfterAddSubtask() {
        manager.addEpic(inputEpic);
        int epicId = inputEpic.getId();

        Subtask inputSubtask = new Subtask("title", "descr", TaskStatus.NEW, Duration.ofMinutes(5000), LocalDateTime.of(2020,10,15,10,10), epicId);
        manager.addSubtask(inputSubtask);
        int subtaskId = inputSubtask.getId();

        Subtask outputSubtask = manager.getSubtaskById(subtaskId);
        assertEquals(inputSubtask, outputSubtask, "Записанная подзадача не соответствует полученной.");

    }

    @Test
    public void shouldReturnTaskListAfterGetTaskList() {
        List<Task> taskList = new ArrayList<>();

        taskList.add(inputTask);
        taskList.add(inputTask2);
        taskList.add(inputTask3);

        manager.addTask(inputTask);
        manager.addTask(inputTask2);
        manager.addTask(inputTask3);


        List<Task> managerTaskList = manager.getTaskList();
        assertEquals(managerTaskList, taskList, "Список тасок не совпадает с ожидаемым.");
    }

    @Test
    public void shouldReturnEmptyTaskListAfterGetEmptyTaskList() {
        List<Task> emptyTaskList = manager.getTaskList();

        assertTrue(emptyTaskList.isEmpty(), "Неверное возвращение списка тасок, когда он пустой. ");
    }

    @Test
    public void shouldReturnEpicListAfterGetEpicList() {
        List<Epic> epicList = new ArrayList<>();

        epicList.add(inputEpic);
        epicList.add(inputEpic2);
        epicList.add(inputEpic3);

        manager.addEpic(inputEpic);
        manager.addEpic(inputEpic2);
        manager.addEpic(inputEpic3);


        List<Epic> managerEpicList = manager.getEpicList();
        assertEquals(managerEpicList, epicList, "Список тасок не совпадает с ожидаемым.");
    }

    @Test
    public void shouldReturnEmptyEpicListAfterGetEmptyEpicList() {
        List<Epic> emptyEpicList = manager.getEpicList();

        assertTrue(emptyEpicList.isEmpty(), "Неверное возвращение списка эпиков, когда он пустой. ");
    }

    @Test
    public void shouldReturnSubtaskListAfterGetSubtaskList() {
        manager.addEpic(inputEpic);
        int epicId = inputEpic.getId();

        List<Subtask> subtaskList = new ArrayList<>();
        Subtask inputSubtask = new Subtask("title", "descr", TaskStatus.NEW,Duration.ofMinutes(5000),LocalDateTime.of(2015,3,3,3,3), epicId);
        Subtask inputSubtask2 = new Subtask("title2", "descr2", TaskStatus.NEW, Duration.ofMinutes(5000), LocalDateTime.of(2014,4,4,4,4), epicId);

        subtaskList.add(inputSubtask);
        subtaskList.add(inputSubtask2);

        manager.addSubtask(inputSubtask);
        manager.addSubtask(inputSubtask2);


        List<Subtask> managerSubtaskList = manager.getSubtaskList();
        assertEquals(managerSubtaskList, subtaskList, "Список тасок не совпадает с ожидаемым.");
    }

    @Test
    public void shouldReturnEmptySubtaskListAfterGetEmptySubtaskList() {
        List<Subtask> emptySubtaskList = manager.getSubtaskList();

        assertTrue(emptySubtaskList.isEmpty(), "Неверное возвращение списка сабтасок, когда он пустой. ");
    }

    @Test
    public void shouldReturnTaskById() {
        List<Task> taskList = new ArrayList<>();
        taskList.add(inputTask);
        taskList.add(inputTask2);
        taskList.add(inputTask3);

        manager.addTask(inputTask);
        manager.addTask(inputTask2);
        manager.addTask(inputTask3);

        int taskId = inputTask.getId();
        int taskId2 = inputTask2.getId();
        int taskId3 = inputTask3.getId();

        Task outputTask = manager.getTaskById(taskId);
        Task outputTask2 = manager.getTaskById(taskId2);
        Task outputTask3 = manager.getTaskById(taskId3);

        assertEquals((taskList.get(0)), outputTask, "Полученная первая таска не соответствует ожидаемой.");
        assertEquals((taskList.get(1)), outputTask2, "Полученная вторая таска не соответствует ожидаемой.");
        assertNotEquals((taskList.get(1)), outputTask3, "Полученная третья таска не должна совпадать со вторым элементом taskList.");
    }

    @Test
    public void shouldReturnTaskNotFoundId1Exception() {

        RuntimeException exp = assertThrows(RuntimeException.class, () -> manager.getTaskById(1));
        assertEquals("Таска с таким id не найдена. Введен id = 1.", exp.getMessage());
    }

    @Test
    public void shouldReturnEpicById() {
        List<Task> epicList = new ArrayList<>();
        epicList.add(inputEpic);
        epicList.add(inputEpic2);
        epicList.add(inputEpic3);

        manager.addEpic(inputEpic);
        manager.addEpic(inputEpic2);
        manager.addEpic(inputEpic3);

        int epicId = inputEpic.getId();
        int epicId2 = inputEpic2.getId();
        int epicId3 = inputEpic3.getId();

        Epic outputEpic = manager.getEpicById(epicId);
        Epic outputEpic2 = manager.getEpicById(epicId2);
        Epic outputEpic3 = manager.getEpicById(epicId3);

        assertEquals((epicList.get(0)), outputEpic, "Полученный первый эпик не соответствует ожидаемому.");
        assertEquals((epicList.get(1)), outputEpic2, "Полученный второй эпик не соответствует ожидаемому.");
        assertNotEquals((epicList.get(1)), outputEpic3, "Полученный третий эпик не должен совпадать со вторым элементом epicList.");
    }

    @Test
    public void shouldReturnEpicNotFoundId1Exception() {

        RuntimeException exp = assertThrows(RuntimeException.class, () -> manager.getEpicById(1));
        assertEquals("Эпик с таким id не найден. Введен id = 1.", exp.getMessage());
    }


    @Test
    public void shouldReturnSubtaskById() {
        manager.addEpic(inputEpic);
        int epicId = inputEpic.getId();

        List<Subtask> subtaskList = new ArrayList<>();
        Subtask inputSubtask = new Subtask("title", "descr", TaskStatus.NEW, Duration.ofMinutes(5000), LocalDateTime.of(2016,5,5,5,5),epicId);
        Subtask inputSubtask2 = new Subtask("title2", "descr2", TaskStatus.NEW, Duration.ofMinutes(5000),LocalDateTime.of(2020,2,2,2,2), epicId);

        subtaskList.add(inputSubtask);
        subtaskList.add(inputSubtask2);

        manager.addSubtask(inputSubtask);
        manager.addSubtask(inputSubtask2);

        int subtaskId = inputSubtask.getId();
        int subtaskId2 = inputSubtask2.getId();

        Subtask outputSubtask = manager.getSubtaskById(subtaskId);
        Subtask outputSubtask2 = manager.getSubtaskById(subtaskId2);

        assertEquals((subtaskList.get(0)), outputSubtask, "Полученная первая подзадача не соответствует ожидаемой.");
        assertNotEquals((subtaskList.get(0)), outputSubtask2, "Полученная вторая таска не должна совпадать с первым элементом subtaskList.");
    }

    @Test
    public void shouldReturnSubtaskNotFoundId1Exception() {

        RuntimeException exp = assertThrows(RuntimeException.class, () -> manager.getSubtaskById(1));
        assertEquals("Сабтаска с таким id не найдена. Введен id = 1.", exp.getMessage());
    }

    @Test
    public void shouldReturnUpdateTask() {

        manager.addTask(inputTask);
        manager.updateTask(1, new Task("newTitle", "newDescr", TaskStatus.IN_PROGRESS, Duration.ofMinutes(5000), LocalDateTime.of(2020,1,1,1,1)));
        Task task = manager.getTaskById(1);

        String expectedTask = "1,TASK,newTitle,IN_PROGRESS,newDescr,PT83H20M,2020-01-01T01:01,2020-01-04T12:21";
        String actualTask = task.toString();

        assertEquals(expectedTask, actualTask, "Полученная таска не соответствует updateTask.");

    }

    @Test
    public void shouldReturnExceptionIfTaskId1NotFoundToUpdate() {
        RuntimeException exp = assertThrows(RuntimeException.class, () ->
                manager.updateTask(1, new Task("newTitle", "newDescr", TaskStatus.IN_PROGRESS, Duration.ofMinutes(5000), LocalDateTime.of(2020,1,1,1,1))));
        assertEquals("Таска с id = 1 не найдена. Изменение не применилось.", exp.getMessage());
    }

    @Test
    public void shouldReturnUpdateEpic() {

        manager.addEpic(inputEpic);
        manager.updateEpic(1, new Epic("newTitle", "newDescr"));
        Epic epic = manager.getEpicById(1);

        String expectedEpic = "1,EPIC,newTitle,NEW,newDescr,null,null,null";
        String actualEpic = epic.toString();

        assertEquals(expectedEpic, actualEpic, "Полученный эпик не соответствует updateEpic.");

    }

    @Test
    public void shouldReturnExceptionIfEpicId1NotFoundToUpdate() {
        RuntimeException exp = assertThrows(RuntimeException.class, () ->
                manager.updateEpic(1, new Epic("newTitle", "newDescr")));
        assertEquals("Эпик с id = 1 не найден. Изменение не применилось.", exp.getMessage());
    }

    @Test
    public void shouldReturnUpdateSubtask() {

        manager.addEpic(inputEpic);
        int epicId = inputEpic.getId();
        Subtask oldSubtask = new Subtask("title", "descr", TaskStatus.IN_PROGRESS, Duration.ofMinutes(5000), LocalDateTime.of(2000,1,1,1,1), epicId);
        manager.addSubtask(oldSubtask);

        Subtask expectedSubtask = new Subtask("newTitle", "newDescr", TaskStatus.NEW, Duration.ofMinutes(5000), LocalDateTime.of(2000,1,1,1,1), epicId);
        expectedSubtask.setId(2);

        manager.updateSubtask(2, new Subtask("newTitle", "newDescr", TaskStatus.NEW,Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1), epicId));
        Subtask actualSubtask = manager.getSubtaskById(2);

        assertEquals(expectedSubtask, actualSubtask, "Полученный эпик не соответствует updateEpic.");

    }

    @Test
    public void shouldReturnExceptionIfSubtaskId1NotFoundToUpdate() {
        manager.addEpic(inputEpic);
        int epicId = inputEpic.getId();
        Subtask oldSubtask = new Subtask("title", "descr", TaskStatus.IN_PROGRESS,Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1), epicId);
        manager.addSubtask(oldSubtask);

        RuntimeException exp = assertThrows(RuntimeException.class, () ->
                manager.updateSubtask(1, new Subtask("newTitle", "newDescr", TaskStatus.NEW,Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1), epicId)));
        assertEquals("Сабтаска с id = 1 не найдена. Изменение не применилось.", exp.getMessage());
    }

    @Test
    public void shouldReturnExceptionIfSubtaskNotContainedInEpicId5ToUpdate() {
        manager.addEpic(inputEpic);
        int epicId = inputEpic.getId();
        Subtask oldSubtask = new Subtask("title", "descr", TaskStatus.IN_PROGRESS,Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1), epicId);
        manager.addSubtask(oldSubtask);
        int subtaskId = oldSubtask.getId();

        RuntimeException exp = assertThrows(RuntimeException.class, () ->
                manager.updateSubtask(subtaskId, new Subtask("newTitle", "newDescr", TaskStatus.NEW,Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1), 5)));
        assertEquals("Сабтаска с id = " + subtaskId + " не связана с указанным эпиком.", exp.getMessage());
    }

    @Test
    public void shouldDeleteAllTasks() {
        manager.addTask(inputTask);
        manager.addTask(inputTask2);
        manager.addTask(inputTask3);

        manager.clearTasks();
        List<Task> taskListAfterClear = manager.getTaskList();

        assertTrue(taskListAfterClear.isEmpty(), "Список тасок после очистки не пустой.");
    }

    @Test
    public void doNothingWhenClearedEmptyTaskList() {
        manager.clearTasks();
        List<Task> taskList = manager.getTaskList();

        assertTrue(taskList.isEmpty(), "Непредвиденное поведение при удалении пустого списка тасок.");
    }

    @Test
    public void shouldDeleteAllEpics() {
        manager.addEpic(inputEpic);
        manager.addEpic(inputEpic2);
        manager.addEpic(inputEpic3);

        manager.clearEpics();
        List<Epic> epicListAfterClear = manager.getEpicList();

        assertTrue(epicListAfterClear.isEmpty(), "Список эпиков после очистки не пустой.");
    }

    @Test
    public void doNothingWhenClearedEmptyEpicList() {
        manager.clearEpics();
        List<Epic> epicList = manager.getEpicList();

        assertTrue(epicList.isEmpty(), "Непредвиденное поведение при удалении пустого списка эпиков.");
    }

    @Test
    public void shouldDeleteAllSubtasksAndSetAllEpicsStatusNew() {
        manager.addEpic(inputEpic);
        manager.addEpic(inputEpic2);
        int epicId = inputEpic.getId();

        Subtask inputSubtask = new Subtask("title", "descr", TaskStatus.NEW,Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1), epicId);
        Subtask inputSubtask2 = new Subtask("title2", "descr2", TaskStatus.NEW, Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1), epicId);

        manager.addSubtask(inputSubtask);
        manager.addSubtask(inputSubtask2);
        int subtaskId = inputSubtask.getId();
        manager.changeSubtaskStatus(subtaskId, TaskStatus.IN_PROGRESS);

        manager.clearSubtasks();
        List<Subtask> subtasksListAfterClear = manager.getSubtaskList();
        List<Epic> actualEpicList = manager.getEpicList();
        boolean epicStatusIsNew = true;
        for (Epic epic : actualEpicList) {
            if (!(epic.getStatus().equals(TaskStatus.NEW))) {
                epicStatusIsNew = false;
                break;
            }
        }

        assertTrue(subtasksListAfterClear.isEmpty(), "Список сабтасок после очистки не пустой.");
        assertTrue(epicStatusIsNew, "Не изменился статус эпиков после удаления всех подзадач.");

    }

    @Test
    public void doNothingWhenClearedEmptySubtaskList() {
        manager.clearSubtasks();
        List<Subtask> subtaskList = manager.getSubtaskList();

        assertTrue(subtaskList.isEmpty(), "Непредвиденное поведение при удалении пустого списка сабтасок.");
    }

    @Test
    public void shouldDeleteTaskById() {

        manager.addTask(inputTask);
        manager.addTask(inputTask2);
        int taskId = inputTask.getId();
        manager.getTaskById(taskId);
        manager.getTaskById(2);
        manager.deleteTaskById(taskId);
        List<Task> actualTaskList = manager.getTaskList();
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(inputTask2);

        assertEquals(expectedTaskList, actualTaskList, "Удаление таски по id некорректно.");
    }

    @Test
    public void shouldReturnExceptionWhenDeleteTaskByMissingId1() {
        RuntimeException exp = assertThrows(RuntimeException.class, () -> manager.deleteTaskById(1));
        assertEquals("Таска с id = 1 не найдена. Таска не удалилась.", exp.getMessage());
    }

    @Test
    public void shouldDeleteEpicById() {

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
    }

    @Test
    public void shouldReturnExceptionWhenTryDeleteEpicByMissingId1() {
        RuntimeException exp = assertThrows(RuntimeException.class, () -> manager.deleteEpicById(1));
        assertEquals("Эпик с id = 1 не найдена. Эпик не удалился.", exp.getMessage());
    }

    @Test
    public void shouldDeleteSubtaskByIdAndSetEpicStatusNewIfSubtaskWasLast() {
        manager.addEpic(inputEpic);
        int epicId = inputEpic.getId();

        Subtask inputSubtask = new Subtask("title", "descr", TaskStatus.NEW, Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1), epicId);
        Subtask inputSubtask2 = new Subtask("title2", "descr2", TaskStatus.NEW, Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1),epicId);

        manager.addSubtask(inputSubtask);
        manager.addSubtask(inputSubtask2);
        int subtaskId = inputSubtask.getId();
        int subtaskId2 = inputSubtask2.getId();

        manager.changeSubtaskStatus(subtaskId, TaskStatus.IN_PROGRESS);

        manager.deleteSubtaskById(subtaskId2);
        List<Subtask> expectedSubtaskList = new ArrayList<>();
        expectedSubtaskList.add(inputSubtask);
        List<Subtask> actualSubtaskList = manager.getSubtaskList();

        assertEquals(expectedSubtaskList, actualSubtaskList, "Удаление сабтаски по id некорректно.");

        manager.deleteSubtaskById(subtaskId);
        assertTrue(manager.getSubtaskList().isEmpty(), "Список сабтасок не пустой после удаления последней сабтаски.");
        TaskStatus actualEpicIsNew = manager.getEpicById(epicId).getStatus();
        assertEquals(TaskStatus.NEW, actualEpicIsNew, "Неверный статус эпика после удаления всех тасок по id.");
    }

    @Test
    public void shouldReturnExceptionWhenDeleteSubtaskByMissingId1() {
        RuntimeException exp = assertThrows(RuntimeException.class, () -> manager.deleteSubtaskById(1));
        assertEquals("Сабтаска с id = 1 не найдена. Сабтаска не удалилась.", exp.getMessage());
    }

    @Test
    public void shouldReturnAllSubtasksFromEpic() {
        manager.addEpic(inputEpic);
        int epicId = inputEpic.getId();

        Subtask inputSubtask = new Subtask("title", "descr", TaskStatus.NEW, Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1), epicId);
        Subtask inputSubtask2 = new Subtask("title2", "descr2", TaskStatus.NEW, Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1), epicId);

        manager.addSubtask(inputSubtask);
        manager.addSubtask(inputSubtask2);

        List<Subtask> expectedSubtaskList = new ArrayList<>();
        expectedSubtaskList.add(inputSubtask);
        expectedSubtaskList.add(inputSubtask2);

        List<Subtask> actualSubtaskList = manager.getAllSubtasksFromEpic(epicId);
        assertEquals(expectedSubtaskList, actualSubtaskList, "Сохраняется не весь список сабтасок.");
    }

    @Test
    public void shouldReturnExceptionWhenEpicWithId5DoesNotFoundToGetAllSubtasksFromEpic() {
        manager.addEpic(inputEpic);
        manager.addEpic(inputEpic2);
        Subtask inputSubtask = new Subtask("title", "descr", TaskStatus.NEW,Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1), 2);
        manager.addSubtask(inputSubtask);

        RuntimeException exp = assertThrows(RuntimeException.class, () -> manager.getAllSubtasksFromEpic(5));
        assertEquals("Эпика с id = 5 не существует. Невозможно получить список всех сабтасок.", exp.getMessage());
    }

    @Test
    public void shouldReturnExceptionWhenEpicWithId1HasNotSubtasksToGetAllSubtasksFromEpic() {
        manager.addEpic(inputEpic);
        manager.addEpic(inputEpic2);
        Subtask inputSubtask = new Subtask("title", "descr", TaskStatus.NEW, Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1),2);
        manager.addSubtask(inputSubtask);

        RuntimeException exp = assertThrows(RuntimeException.class, () -> manager.getAllSubtasksFromEpic(1));
        assertEquals("У эпика с id = 1 нет сабтасок. Невозможно получить список всех сабтасок.", exp.getMessage());
    }

    @Test
    public void shouldChangeSubtaskStatus() {
        manager.addEpic(inputEpic);
        Subtask inputSubtask = new Subtask("title", "descr", TaskStatus.NEW, Duration.ofMinutes(5000),  LocalDateTime.of(2000,1,1,1,1),1);
        manager.addSubtask(inputSubtask);

        manager.changeSubtaskStatus(2, TaskStatus.IN_PROGRESS);

        TaskStatus subtaskStatus = inputSubtask.getStatus();
        TaskStatus epicStatus = inputEpic.getStatus();

        assertEquals(subtaskStatus, TaskStatus.IN_PROGRESS, "Статус сабтаски не изменился.");

        assertEquals(epicStatus, TaskStatus.IN_PROGRESS, "Не изменился статус эпика при изменении статуса сабтаски.");
    }

    @Test
    public void shouldReturnExceptionWhenChangeStatusMissingSubtaskId1() {
        RuntimeException exp = assertThrows(RuntimeException.class, () -> manager.changeSubtaskStatus(1, TaskStatus.DONE));
        assertEquals("Сабтаска с id = 1 не найдена. Изменить статус сабтаски невозможно.", exp.getMessage());
    }


}