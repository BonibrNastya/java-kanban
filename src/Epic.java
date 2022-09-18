import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    public Epic(String title, String description) {
        super(title, description, TaskStatus.NEW);
    }

    private List<Subtask> subtaskMap = new ArrayList<>();

    public List<Subtask> getSubtaskList() {
        return subtaskMap;
    }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
