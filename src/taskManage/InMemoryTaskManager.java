package taskManage;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    @Override
    public ArrayList<Task> getTasks() { return new ArrayList<>(tasks.values()); }

    @Override
    public ArrayList<SubTask> getSubTasks() { return new ArrayList<>(subTasks.values()); }

    @Override
    public ArrayList<Epic> getEpics() { return new ArrayList<>(epics.values()); }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTasksId().clear();
            epic.setStatus(Status.NEW);
        }
        subTasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        Managers.getDefaultHistory().add(task);
        return task;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        Managers.getDefaultHistory().add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        Managers.getDefaultHistory().add(epic);
        return epic;
    }

    @Override
    public ArrayList<SubTask> getSubTasksByEpicId(int id) {
        ArrayList<SubTask> result = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == id) {
                result.add(subTask);
            }
        }
        return result;
    }

    @Override
    public void addTask(Task task) {
        if (task.getClass() == Task.class) {
            id += 1;
            task.setId(id);
            tasks.put(id, task);
        } else {
            throw new IllegalArgumentException("This is not task!");
        }
    }

    @Override
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

    @Override
    public void addEpic(Epic epic) {
        id += 1;
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())){
            tasks.put(task.getId(), task);
        } else {
            throw new IllegalArgumentException("This task is not contain to tasks!");
        }
    }

    @Override
    public void updateSubTask(SubTask subtask){
        if (subtask.getEpicId() == subtask.getId()) {
            throw new IllegalArgumentException("epicId != subtaskId");
        }
        if (!subTasks.containsKey(subtask.getId())) {
            throw new IllegalArgumentException("Subtask with id " + subtask.getId() + " not found");
        } else {
            subTasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic == null) {
                throw new IllegalArgumentException("Epic is null");
            }
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

    @Override
    public void updateEpic(Epic epic){
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        int epicId = subTasks.get(id).getEpicId();
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subIds = epic.getSubTasksId();
        subIds.remove(id);
        subTasks.remove(id);
        updateEpicStatus(epic);
    }

    @Override
    public void deleteEpicById(int id) {
        epics.remove(id);
        for (SubTask subTask : new HashMap<>(subTasks).values()) {
            if (subTask.getEpicId() == id) {
                subTasks.remove(subTask.getId());
            }
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        return Managers.getDefaultHistory().getHistory();
    }
}
