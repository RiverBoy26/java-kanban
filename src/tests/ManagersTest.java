package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskManage.InMemoryHistoryManager;
import taskManage.InMemoryTaskManager;
import taskManage.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.LinkedList;
import java.util.List;

class ManagersTest {
    private InMemoryTaskManager manager;
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        manager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void managersReturnInitializedInstances() {
        Assertions.assertNotNull(Managers.getDefault());
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }

    @Test
    void inMemoryManagerAddsDifferentTaskTypesAndFindsById() {
        Task t = new Task("task", "1");
        manager.addTask(t);
        Task fromManager = manager.getTask(t.getId());
        Assertions.assertEquals(t, fromManager);
        Epic e = new Epic("epic", "2");
        manager.addEpic(e);
        Epic fromEpic = manager.getEpic(e.getId());
        Assertions.assertEquals(e, fromEpic);
        SubTask s = new SubTask("subtask", "3", e);
        manager.addSubTask(s);
        SubTask fromSub = manager.getSubTask(s.getId());
        Assertions.assertEquals(s, fromSub);
    }

    @Test
    void tasksWithManualAndGeneratedIdDoNotConflict() {
        Task manualTask = new Task("manual", "1");
        manager.addTask(manualTask);
        manualTask.setId(999);
        Task generatedTask = new Task("generate", "2");
        manager.addTask(generatedTask);
        Assertions.assertNotEquals(manualTask.getId(), generatedTask.getId());
        Assertions.assertTrue(manager.getTasks().contains(manualTask));
        Assertions.assertTrue(manager.getTasks().contains(generatedTask));
    }

    @Test
    void tasksRemainUnchangedWhenAddedExceptId() {
        Task t = new Task("task", "description");
        t.setStatus(Status.IN_PROGRESS);
        String nameBefore = t.getTaskName();
        String descBefore = t.getDescription();
        Status statusBefore = t.getStatus();
        manager.addTask(t);
        Assertions.assertEquals(nameBefore, t.getTaskName());
        Assertions.assertEquals(descBefore, t.getDescription());
        Assertions.assertEquals(statusBefore, t.getStatus());
        Assertions.assertTrue(t.getId() > 0);
    }

    @Test
    void historyStoresPreviousVersionOfTask() {
        Task taskOriginal = new Task("text", "o");
        manager.addTask(taskOriginal);
        int id = taskOriginal.getId();
        Task fetched = manager.getTask(id);
        Assertions.assertEquals(taskOriginal, fetched);
        Task updated = new Task("updated", "u");
        updated.setId(id);
        manager.updateTask(updated);
        List<Task> history = manager.getHistory();
        Assertions.assertFalse(history.isEmpty());
        Task histTask = history.getLast();
        Assertions.assertEquals("text", histTask.getTaskName());
        Assertions.assertEquals("o", histTask.getDescription());
    }

    @Test
    void updateNonExistingTaskThrows() {
        Task t = new Task("n", "d");
        t.setId(99999);
        Assertions.assertThrows(IllegalArgumentException.class, () -> manager.updateTask(t));
    }

    @Test
    void deletingEpicRemovesSubtasks() {
        Epic e = new Epic("Epic", "del");
        manager.addEpic(e);
        SubTask s1 = new SubTask("s1", "a", e);
        SubTask s2 = new SubTask("s2", "b", e);
        manager.addSubTask(s1);
        manager.addSubTask(s2);
        int epicId = e.getId();
        int id1 = s1.getId();
        int id2 = s2.getId();
        manager.deleteEpicById(epicId);
        Assertions.assertNull(manager.getEpic(epicId));
        Assertions.assertNull(manager.getSubTask(id1));
        Assertions.assertNull(manager.getSubTask(id2));
    }

    @Test
    void addTaskShouldBeAccessOnlyTask() {
        Task t = new Task("n","d");
        manager.addTask(t);
        Assertions.assertNotNull(manager.getTasks());
        Assertions.assertEquals(1, manager.getTasks().size());
        Assertions.assertEquals(t, manager.getTasks().getFirst());
        Epic e = new Epic("n", "d");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            manager.addTask(e);
        });
        SubTask s = new SubTask("n", "d", e);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            manager.addTask(s);
        });
    }

    @Test
    void InHistoryManagerShouldBeAddAndFindTask() {
        Task t1 = new Task("n","d");
        manager.addTask(t1);
        Epic t2 = new Epic("n","d");
        manager.addEpic(t2);
        SubTask t3 = new SubTask("n","d", t2);
        manager.addSubTask(t3);
        manager.getTask(t1.getId());
        manager.getEpic(t2.getId());
        manager.getSubTask(t3.getId());
        Assertions.assertEquals(manager.getHistory().getFirst(), t1);
        Assertions.assertTrue(manager.getHistory().contains(t2));
        Assertions.assertEquals(manager.getHistory().getLast(), t3);
    }

    @Test
    void InHistoryManagerShouldBeRemoveTask() {
        Task t1 = new Task("n","d");
        manager.addTask(t1);
        Epic t2 = new Epic("n","d");
        manager.addEpic(t2);
        manager.getTask(t1.getId());
        manager.getTask(t1.getId());
        manager.getEpic(t2.getId());
        Assertions.assertEquals(manager.getHistory().getFirst(), t1);
        manager.getTask(t1.getId());
        Assertions.assertEquals(manager.getHistory().getFirst(), t2);
        Assertions.assertEquals(manager.getHistory().getLast(), t1);
    }

    @Test
    void HistoryShouldBeUnlimited() {
        for (int i = 1; i <= 100; i++) {
            Task task = new Task( "Task " + i, "Description " + i);
            manager.addTask(task);
            manager.getTask(task.getId());
        }
        Assertions.assertEquals(100, manager.getHistory().size());
        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i + 1, manager.getHistory().get(i).getId());
        }
    }

    @Test
    void InHistoryManagerRemoveNoneTask() {
        Task t1 = new Task("n","d");
        manager.addTask(t1);
        manager.getTask(t1.getId());
        historyManager.remove(99);
        Assertions.assertEquals(1, manager.getHistory().size());
    }

    @Test
    void InHistoryManagerRemoveFromEmptyHistory() {
        historyManager.remove(0);
        Assertions.assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    public void InHistoryManagerRemoveOnlyOneTask() {
        Task t1 = new Task("n","d");
        manager.addTask(t1);
        manager.getTask(t1.getId());
        historyManager.remove(1);
        Assertions.assertEquals(0, historyManager.getHistory().size());
    }
}
