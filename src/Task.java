public class Task {
    String taskName;
    String Description;
    private int id;
    TaskManager taskManager = new TaskManager();
    Status status;

    public Task(String taskName, String description, TaskManager taskManager) {
        this.taskName = taskName;
        this.Description = description;
        this.taskManager = taskManager;
        this.status = Status.NEW;
        if (this.getClass() == Task.class) {
            taskManager.addTask(this);
        }
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

    public void updateStatus() {
        if (this.status == Status.IN_PROGRESS) {
            this.status = Status.DONE;
        } else if (this.status != Status.DONE){
            this.status = Status.IN_PROGRESS;
        }
    }
}
