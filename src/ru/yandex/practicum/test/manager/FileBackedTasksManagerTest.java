package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import models.Subtask;
import models.Task;
import models.TaskStatus;
import utils.Managers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private final String HOME = "src/ru/yandex/practicum/main/resources/test" + System.nanoTime() + ".csv";
    private final Path testFile = Path.of(HOME);
    private final File file = new File(String.valueOf(testFile));
    private BufferedReader br;
    private String line;


    @BeforeEach
    public void setUp() {

        manager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);

    }

    @AfterEach
    protected void tearDown() {
        try {
            Files.delete(testFile);
        } catch (IOException e) {
            assertFalse(Files.exists(testFile), "Файл не удалился.");

        }
    }

    @Test
    public void saveTaskInFileAfterAdd() throws IOException {
        manager.addTask(inputTask);

        try {
            br = new BufferedReader(new FileReader(HOME));
            line = br.readLine();
        } finally {
            try {
                br.close();
            } catch (IOException exp) {
                System.out.println(exp.getMessage());
            }
        }
        assertFalse(line.isEmpty(), "Таска не записалась в файл.");

    }

    @Test
    public void saveEpicInFileAfterAdd() throws IOException {
        manager.addEpic(inputEpic);

        try {
            br = new BufferedReader(new FileReader(HOME));
            line = br.readLine();
        } finally {
            try {
                br.close();
            } catch (IOException exp) {
                System.out.println(exp.getMessage());
            }
        }
        assertFalse(line.isEmpty(), "Эпик не записался в файл.");

    }

    @Test
    public void saveSubtaskInFileAfterAdd() throws IOException {
        manager.addEpic(inputEpic);
        manager.addSubtask(new Subtask("Sub 1", "Sub Dscr 1", TaskStatus.NEW, 5000L, LocalDateTime.of(2020, 10, 10, 10, 10), 1));
        String[] savedLine = null;

        try {
            br = new BufferedReader(new FileReader(file));
            while (!(line = br.readLine()).isEmpty()) {
                savedLine = line.split(",");
            }
        } finally {
            try {
                br.close();
            } catch (IOException exp) {
                System.out.println(exp.getMessage());
            }
        }
        assert savedLine != null;
        String subtask = savedLine[1];
        assertEquals("SUBTASK", subtask, "Сабтаска не записалась в файл.");

    }

    @Test
    public void shouldReturnLoadingTaskList() throws IOException {

        manager.addTask(inputTask2);
        manager.loadFromFile();
        List<Task> taskList = manager.getTaskList();
        assertFalse(taskList.isEmpty(), "empty taskList");
    }


}