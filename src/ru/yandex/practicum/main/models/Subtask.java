package models;

import models.enums.TaskStatus;
import models.enums.TaskType;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, String description, TaskStatus status,
                   long duration, LocalDateTime startTime, int epicId) {
        super(title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return id + "," + TaskType.SUBTASK + "," + title + "," + status + "," + description + "," +
                duration + "," + startTime + "," + endTime + "," + epicId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Subtask otherSubtask = (Subtask) obj;
        return Objects.equals(status, otherSubtask.status) &&
                Objects.equals(title, otherSubtask.title) &&
                Objects.equals(description, otherSubtask.description) &&
                (id == otherSubtask.id) &&
                (duration == otherSubtask.duration)&&
                Objects.equals(startTime, otherSubtask.startTime)&&
                Objects.equals(endTime, otherSubtask.endTime)&&
                (epicId == otherSubtask.epicId);
    }
}
