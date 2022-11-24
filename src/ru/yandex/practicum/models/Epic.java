package ru.yandex.practicum.models;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    @Override
    public String toString() {
        return id + "," + TaskType.EPIC + "," + title + "," + status + "," + "Description " + description;
    }

    private List<Subtask> subtaskMap = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
    }

    public List<Subtask> getSubtaskList() {
        return subtaskMap;
    }
}
