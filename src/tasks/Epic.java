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


}
