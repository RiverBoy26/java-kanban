package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    final private ArrayList<Integer> subTasksId;

    public Epic(String title, String description) {
        super(title, description);
        this.subTasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksId() {
        return subTasksId;
    }

    public void addSubTask(SubTask subTask) {
        int subId = subTask.getId();
        if (!subTasksId.contains(subId)) {
            subTasksId.add(subId);
            subTask.setEpicId(getId());
        }
    }

    public void deleteSubTask(SubTask subTask) {
        for (Integer subId : subTasksId) {
            if (subId.equals(subTask.getId())) {
                subTasksId.remove(subId);
                break;
            }
        }
    }

    public void subtasksClear() {
        subTasksId.clear();
    }
}
