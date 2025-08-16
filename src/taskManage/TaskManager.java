package taskManage;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int id = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    public ArrayList<Task> getTasks() { return new ArrayList<>(tasks.values()); }

    public ArrayList<SubTask> getSubTasks() { return new ArrayList<>(subTasks.values()); }

    public ArrayList<Epic> getEpics() { return new ArrayList<>(epics.values()); }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTasksId().clear();
            epic.setStatus(Status.NEW);
        }
        subTasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public ArrayList<SubTask> getSubTasksByEpicId(int id) {
        ArrayList<SubTask> result = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == id) {
                result.add(subTask);
            }
        }
        return result;
    }

    public void addTask(Task task) {
        id += 1;
        task.setId(id);
        tasks.put(id, task);
    }

    public void addSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())){
            id += 1;
            subTask.setId(id);
            subTasks.put(id, subTask);
            Epic epic = epics.get(subTask.getEpicId());
            epic.getSubTasksId().add(subTask.getId());
            updateEpicStatus(epic);
        }
    }

    public void addEpic(Epic epic) {
        id += 1;
        epic.setId(id);
        epics.put(id, epic);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())){
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubTask(SubTask subtask){
        if (subTasks.containsKey(subtask.getId())) {
            subTasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            updateEpicStatus(epic);
        }
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> allSubTasks = epic.getSubTasksId();
        if (allSubTasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        boolean allDone = true;
        boolean isNew = true;
        boolean isInProgress = false;
        for (Integer subtaskId : allSubTasks) {
            SubTask subtask = subTasks.get(subtaskId);
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
            if (subtask.getStatus() != Status.NEW) {
                isNew = false;
            } else if (subtask.getStatus() == Status.IN_PROGRESS) {
                isInProgress = true;
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (!isNew || isInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }

    public void updateEpic(Epic epic){
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteSubTaskById(Integer id) {
        int epicId = subTasks.get(id).getEpicId();
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subIds = epic.getSubTasksId();
        subIds.remove(id);
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
        for (SubTask subTask : new HashMap<>(subTasks).values()) {
            if (subTask.getEpicId() == id) {
                subTasks.remove(subTask.getId());
            }
        }
    }
}
