import taskManage.InMemoryTaskManager;
import taskManage.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Задача 1", "ААА");
        Task task2 = new Task("Задача 2", "БББ");
        Epic epic1 = new Epic("Эпик 1", "ЭПИЧНО");
        Epic epic2 = new Epic("Эпик 2", "ЭПИЧНО");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Подзадача 1", "111", epic1);
        SubTask subTask2 = new SubTask("Подзадача 2", "222", epic1);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addEpic(epic2);
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task2.getId());
        SubTask subTask11 = new SubTask("Подзадача 1.1", "11111", epic1);
        taskManager.addSubTask(subTask11);
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubTasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
