package tasks;

public class Task {
    private String taskName;
    private String Description;
    private int id;
    private Status status;

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.Description = description;
        this.status = Status.NEW;
    }

    public String getTaskName() {
        return taskName;
    }
    public String getDescription() {
        return Description;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
