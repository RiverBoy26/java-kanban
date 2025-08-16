package tasks;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String title, String description, Epic epic) {
        super(title, description);
        this.epicId = epic.getId();
    }
    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int id) {
        epicId = id;
    }
}
