package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskManage.InMemoryTaskManager;
import taskManage.Managers;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

class SubTaskTest {
    private static SubTask subTask1;
    private static SubTask subTask2;
    private static Epic epic;
    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeAll
    static void beforeAll() {
        epic = new Epic("1", "2");
        subTask1 = new SubTask("1", "2", epic);
        subTask2 = new SubTask("1", "2", epic);
    }

    @BeforeEach
    void setManage() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    void addNewTask() {
        inMemoryTaskManager.addEpic(epic);
        SubTask subTask = new SubTask("Test addNewSubTask", "Test addNewSubTask description", epic);
        inMemoryTaskManager.addSubTask(subTask);
        final int taskId = subTask.getId();
        final Task savedTask = inMemoryTaskManager.getSubTask(taskId);

        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(subTask, savedTask, "Задачи не совпадают.");

        final List<SubTask> subtasks = inMemoryTaskManager.getSubTasks();

        Assertions.assertNotNull(subtasks, "Задачи не возвращаются.");
        Assertions.assertEquals(1, subtasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(subTask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void twoEpicsShouldBeEquals() {
        subTask1.setId(1);
        subTask2.setId(1);
        Assertions.assertEquals(subTask1, subTask2);
    }

    @Test
    void subtaskIsNotShouldBeHisEpic() {
        inMemoryTaskManager.addEpic(epic);
        inMemoryTaskManager.addSubTask(subTask1);
        int subTaskId = subTask1.getId();
        subTask1.setEpicId(subTaskId);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            inMemoryTaskManager.updateSubTask(subTask1);
        });
    }
}