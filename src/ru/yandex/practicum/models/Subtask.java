package ru.yandex.practicum.models;

import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, TaskStatus status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return id + "," + TaskType.SUBTASK + "," + title + "," + status + "," + description + "," + epicId;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Subtask otherSubtask = (Subtask) obj;
        return Objects.equals(status, otherSubtask.status) &&
                Objects.equals(title, otherSubtask.title) &&
                Objects.equals(description, otherSubtask.description) &&
                (id == otherSubtask.id) &&
                (epicId == otherSubtask.epicId);
    }
}
