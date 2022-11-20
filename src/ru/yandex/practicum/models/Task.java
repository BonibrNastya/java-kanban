package ru.yandex.practicum.models;

import static ru.yandex.practicum.models.TaskType.TASK;

public class Task {
    protected int id;
    protected String title;
    protected String description;
    protected TaskStatus status;

    @Override
    public String toString() {
        return id + "," + TASK + "," + title + "," + status + "," + "Description " + description + ",";
    }

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
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
        switch (status){
            case TASK:
                task = new Task(split[2], split[4], TaskStatus.valueOf(split[3]));
                task.setId(Integer.parseInt(split[0]));
                return task;
            case EPIC:
                task = new Epic(split[2], split[4]);
                task.setId(Integer.parseInt(split[0]));
                return task;
            case SUBTASK:
                task = new Subtask(split[2], split[4], TaskStatus.valueOf(split[3]), Integer.parseInt(split[5]));
                task.setId(Integer.parseInt(split[0]));
                return task;
        }
        return null;
    }
}