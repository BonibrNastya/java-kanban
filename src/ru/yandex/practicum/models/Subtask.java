package ru.yandex.practicum.models;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, TaskStatus status) {
        super(title, description, status);
    }

    public int getEpicId() {
        return epicId;
    }
}
