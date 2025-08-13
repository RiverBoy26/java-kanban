import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TaskManager {
    static int id = 0;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }
    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
    }

    public void deleteSubTasksByEpic(Epic epic) {
        for (SubTask subTask : new HashMap<>(subTasks).values()) {
            if (subTask.getEpicId() == epic.getId()) {
                subTasks.remove(subTask.getId());
            }
            epic.updateStatus();
        }
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
    public Epic getEpicBySubTaskId(int id) {
        SubTask subTask = subTasks.get(id);
        return epics.get(subTask.getEpicId());
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
            Epic epic = getEpicBySubTaskId(subTask.getId());
            epic.updateStatus();
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
            Epic epic = getEpicBySubTaskId(subtask.getId());
            epic.updateStatus();
        }
    }

    public void updateEpic(Epic epic){
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            epic.updateStatus();
        }
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteSubTaskById(int id) {
        subTasks.remove(id);
        for (Epic epic : epics.values()) {
            ArrayList<Integer> SubIds = epic.getSubTasksId();
            for (Integer SubId : SubIds) {
                if (SubId == id) {
                    SubIds.remove(id);
                    epic.updateStatus();
                }
            }
        }
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
