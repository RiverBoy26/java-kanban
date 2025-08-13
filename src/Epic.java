import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    final private ArrayList<Integer> subTasksId;

    public Epic(String title, String description, TaskManager taskManager) {
        super(title, description, taskManager);
        this.subTasksId = new ArrayList<>();
        taskManager.addEpic(this);
    }

    @Override
    public void updateStatus() {
        HashMap<Integer, SubTask> allSubTasks = taskManager.getSubTasks();
        for (Integer subtaskId : allSubTasks.keySet()) {
            SubTask subtask1 = allSubTasks.get(subtaskId);
            if (subtask1 != null) {
                if (subtask1.getStatus() != Status.DONE) {
                    this.status = Status.IN_PROGRESS;
                } else {
                    this.status = Status.DONE;
                }
            }
        }
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void addSubTaskInEpic(int id){
        subTasksId.add(id);
    }
}
