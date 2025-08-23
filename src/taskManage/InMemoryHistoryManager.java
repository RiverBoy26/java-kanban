package taskManage;

import tasks.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> historyOfTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyOfTasks.size() == 10) {
            historyOfTasks.removeFirst();
        }
        historyOfTasks.addLast(task);
    }

    @Override
    public LinkedList<Task> getHistory() {
        return historyOfTasks;
    }
}
