public class Main {

    public static void main(String[] args) {
        // Создание задач
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Задача 1", "ААА", taskManager);
        Task task2 = new Task("Задача 2", "БББ", taskManager);
        Epic epic1 = new Epic("Эпик 1", "ЭПИЧНО", taskManager);
        Epic epic2 = new Epic("Эпик 2", "ЭПИЧНО", taskManager);
        SubTask subTask1 = new SubTask("Подзадача 1", "111", epic1, taskManager);
        SubTask subTask2 = new SubTask("Подзадача 2", "222", epic1, taskManager);


        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubTasks());

        taskManager.deleteSubTaskById(subTask1.getId());

        System.out.println();
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubTasks());

    }
}
