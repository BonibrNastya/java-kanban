package ru.yandex.practicum.utils;

import ru.yandex.practicum.manager.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultFile() throws IOException {
        FileBackedTasksManager file;
        try {
            file = new FileBackedTasksManager(Files.createFile(Path.of("src/ru/yandex/practicum/resources/fileBacked.csv")));

        } catch (FileAlreadyExistsException exp) {
            return file = FileBackedTasksManager.loadFromFile(new File("src/ru/yandex/practicum/resources/fileBacked.csv"));
        }
        return file;
    }
}


