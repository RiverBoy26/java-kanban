package taskManage;

import tasks.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private final int MAX_SIZE = 10;
    private final LinkedList<Task> historyOfTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyOfTasks.size() == MAX_SIZE) {
            historyOfTasks.removeFirst();
        }
        historyOfTasks.addLast(task);
    }

    @Override
    public LinkedList<Task> getHistory() {
        return historyOfTasks;
    }
}
