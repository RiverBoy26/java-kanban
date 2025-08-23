package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskManage.InMemoryTaskManager;
import tasks.Task;

import java.util.List;

class TaskTest {
    @Test
    void twoTasksShouldBeEquals() {
        Task task1 = new Task("1", "2");
        Task task2 = new Task("1", "2");
        task1.setId(111);
        task2.setId(111);
        Assertions.assertEquals(task1, task2);
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.addTask(task);
        final int taskId = task.getId();
        final Task savedTask = manager.getTask(taskId);
        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = manager.getTasks();
        Assertions.assertNotNull(tasks, "Задачи не возвращаются.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }


}