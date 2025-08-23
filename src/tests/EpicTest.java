package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskManage.InMemoryTaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

class EpicTest {
    private static Epic epic1;
    private static Epic epic2;
    private InMemoryTaskManager inMemoryTaskManager;

    @BeforeAll
    static void beforeAll() {
        epic1 = new Epic("1", "2");
        epic2 = new Epic("1", "2");
    }

    @BeforeEach
    void setManage() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        inMemoryTaskManager.addEpic(epic);
        final int taskId = epic.getId();
        final Task savedTask = inMemoryTaskManager.getEpic(taskId);

        Assertions.assertNotNull(savedTask, "Эпики не найдены.");
        Assertions.assertEquals(epic, savedTask, "Эпики не совпадают.");

        final List<Epic> epics = inMemoryTaskManager.getEpics();

        Assertions.assertNotNull(epics, "Эпики не возвращаются.");
        Assertions.assertEquals(1, epics.size(), "Неверное количество эпиков.");
        Assertions.assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void twoEpicsShouldBeEquals() {
        epic1.setId(1);
        epic2.setId(1);
        Assertions.assertEquals(epic1, epic2);
    }

    @Test
    void shouldNotAddEpicToItselfAsSubtask() {
        inMemoryTaskManager.addEpic(epic2);
        SubTask invalidSubtask = new SubTask("Invalid", "Description", epic2);
        inMemoryTaskManager.addSubTask(invalidSubtask);
        int epicId = epic2.getId();
        invalidSubtask.setId(epicId);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            inMemoryTaskManager.updateSubTask(invalidSubtask);
        });
    }
}