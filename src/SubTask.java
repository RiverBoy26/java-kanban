public class SubTask extends Task {
    final private int epicId;

    public SubTask(String title, String description, Epic epic, TaskManager taskmanager) {
        super(title, description, taskmanager);
        this.epicId = epic.getId();
        Epic epic1 = taskmanager.getEpic(epicId);
        epic1.addSubTaskInEpic(getId());
        taskmanager.addSubTask(this);
    }
    public int getEpicId() {
        return epicId;
    }

    @Override
    public void updateStatus() {
        if (this.status == Status.IN_PROGRESS) {
            this.status = Status.DONE;
        } else if (this.status != Status.DONE){
            this.status = Status.IN_PROGRESS;
        }
        Epic epic = taskManager.getEpic(epicId);
        epic.updateStatus();
    }

}
