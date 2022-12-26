import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import manager.FileBackedTasksManager;
import models.Subtask;
import models.Task;
import models.TaskStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private Path testFile;
    private File file;
    private final String HOME = "src/ru/yandex/practicum/main/resources/test" + System.nanoTime() + ".csv";
    private String home;
    private BufferedReader br;
    private String line;


    @BeforeEach
    public void setUp() throws IOException {
        home = HOME;
        testFile = Files.createFile(Path.of(home));
        manager = new FileBackedTasksManager(testFile);

    }

    @AfterEach
    protected void tearDown() throws IOException {

        System.gc();
        Files.delete(testFile);
        assertFalse(Files.exists(Paths.get(home)), "Файл не удалился.");
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
        manager.addSubtask(new Subtask("Sub 1", "Sub Dscr 1", TaskStatus.NEW, Duration.ofMinutes(5000), LocalDateTime.of(2020,10,10,10,10), 1));
        String[] savedLine = null;

        try {
            br = new BufferedReader(new FileReader(home));
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
        file = Path.of("src/ru/yandex/practicum/main/resources/fileBacked.csv").toFile();

        FileBackedTasksManager fileManager = manager.loadFromFile(file);
        Task task = fileManager.getTaskMap().get(1);
        assertNotEquals("TASK", task, "empty");
    }

    @Test
    public void shouldNotLoadTaskAndHistoryWhenLoadingFileBackedEmpty() throws IOException {
        file = Path.of("src/ru/yandex/practicum/main/resources/fileBackedEmpty.csv").toFile();
        FileBackedTasksManager fileManager = manager.loadFromFile(file);
        List<Task> taskList = fileManager.getTaskList();
        List<Task> historyList = fileManager.getHistoryManager().getHistory();

        assertTrue(taskList.isEmpty(), "Неправильная работа восстановления из файла, когда список задач пустой.");
        assertTrue(historyList.isEmpty(), "Неправильная работа восстановления из файла, когда список историй пустой.");
    }

    @Test
    public void shouldReturnEpicWithoutSubtasksFromFileBackedEmptyEpic() throws IOException {
        file = Path.of("src/ru/yandex/practicum/main/resources/fileBackedEmptyEpic.csv").toFile();
        FileBackedTasksManager fileManager = manager.loadFromFile(file);
        String actualEpic = fileManager.getEpicById(1).toString();
        String expectedEpic = "1,EPIC,Epic 1,NEW,Epic Dscr 1,null,null,null";

        assertEquals(expectedEpic, actualEpic, "Неправильное восстановление эпика.");
    }

}