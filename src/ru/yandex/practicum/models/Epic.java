package ru.yandex.practicum.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    @Override
    public String toString() {
        return id + "," + TaskType.EPIC + "," + title + "," + status + "," + description + ","
                + duration + "," + startTime + "," + endTime;
    }

    private final List<Subtask> subtaskMap = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW, null, null);
    }

    public List<Subtask> getSubtaskList() {
        return subtaskMap;
    }




    public final void updateEpicTime(Map<Integer, Subtask> subtasks) {

        Comparator<LocalDateTime> localDateTimeComparator = LocalDateTime::compareTo;

        Optional<LocalDateTime> epicStartTime = subtasks.values()
                .stream().map(Subtask::getStartTime).min(localDateTimeComparator);

        setStartTime(epicStartTime.orElse(null));

        Optional<LocalDateTime> epicEndTime = subtasks.values()
                .stream().map(Subtask::getEndTime).max(localDateTimeComparator);

        if (epicStartTime.isPresent() && epicEndTime.isPresent()) {
            setDuration(Duration.between(epicStartTime.get(), epicEndTime.get()));
        }
    }
}

