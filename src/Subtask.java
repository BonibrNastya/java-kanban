public class Subtask extends Task{
    private int epicId;
    private int id;
    private String title;
    private String description;
    private TaskStatus status;

    public Subtask(String title, String description, TaskStatus status) {
        super(title, description, status);

    }

    public int getEpicId() {
        return epicId;
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
