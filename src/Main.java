import taskManage.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        // Создание задач
        TaskManager taskManager = new TaskManager();
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
        SubTask subTask11 = new SubTask("Подзадача 1.1", "11111", epic1);
        taskManager.addSubTask(subTask11);

        subTask1.setStatus(Status.DONE);
        subTask11.setStatus(Status.DONE);
        subTask2.setStatus(Status.NEW);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);
        taskManager.updateSubTask(subTask11);

        System.out.println("Subtask 1: " + subTask1.getStatus());
        System.out.println("Subtask 2: " + subTask2.getStatus());
        System.out.println("Subtask 11: " + subTask11.getStatus());
        System.out.println("Epic 1: " + epic1.getStatus());
    }
}
