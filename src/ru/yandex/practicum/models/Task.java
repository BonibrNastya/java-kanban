package ru.yandex.practicum.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static ru.yandex.practicum.models.TaskType.TASK;

public class Task {
    protected int id;
    protected String title;
    protected String description;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public LocalDateTime getEndTime() {

        return endTime = startTime.plus(duration);
    }

    @Override
    public String toString() {
        return id + "," + TASK + "," + title + "," + status + "," + description + ","
                + duration + "," + startTime + "," + endTime;
    }

    public Task(String title, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public static Task fromString(String value) {
        String[] split = value.split(",");
        TaskType status = TaskType.valueOf(split[1]);
        Task task;
        switch (status) {
            case TASK:
                task = new Task(split[2], split[4], TaskStatus.valueOf(split[3]),
                        Duration.parse(split[5]), LocalDateTime.parse(split[6]));
                task.setId(Integer.parseInt(split[0]));
                return task;
            case EPIC:
                task = new Epic(split[2], split[4]);
                task.setId(Integer.parseInt(split[0]));
                return task;
            case SUBTASK:
                task = new Subtask(split[2], split[4], TaskStatus.valueOf(split[3]),
                        Duration.parse(split[5]), LocalDateTime.parse(split[6]), Integer.parseInt(split[7]));
                task.setId(Integer.parseInt(split[0]));
                return task;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(status, otherTask.status) &&
                Objects.equals(title, otherTask.title) &&
                Objects.equals(description, otherTask.description) &&
                (id == otherTask.id) &&
                (Objects.equals(duration, otherTask.duration)) &&
                Objects.equals(startTime, otherTask.startTime) &&
                Objects.equals(endTime, otherTask.endTime);
    }
}