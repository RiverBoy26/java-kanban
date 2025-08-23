package tasks;

public class Task {
    private final String taskName;
    private final String description;
    private int id;
    private Status status;

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        this.status = Status.NEW;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof Task other)){
            return false;
        }
        if (this.id == 0 || other.id == 0){
            return false;
        }
        return this.id == other.id;
    }
}
