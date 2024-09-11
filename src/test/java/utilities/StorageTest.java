package utilities;

import java.util.ArrayList;

import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

// Assumes the data file is based on the default generated by runtest
public class StorageTest {
    private static final String FILE_PATH = "tasks/data.txt"; 

    @Test
    public void loadTasksFromFileTest() {
        Storage store = new Storage(FILE_PATH);
        ArrayList<Task> tl = store.loadTaskListFromFile();
        // Check for length of tasks read
        assertEquals(tl.size(), 4);

        // Check for correct datetime of event read
        Task t = tl.get(2);
        assertEquals("2024-12-29 12:00,2024-12-29 17:00", t.getWriteTaskInfo());
    }

    @Test
    public void updateTaskStatusInFileTest() {
        Storage store = new Storage(FILE_PATH);
        store.updateTaskStatus(3, true);
        // Check for the correct update of task status
        ArrayList<Task> tl = store.loadTaskListFromFile();
        Task t = tl.get(3);
        assertEquals("X", t.getTaskStatus());

        store.updateTaskStatus(2, false);
        ArrayList<Task> tl2 = store.loadTaskListFromFile();
        Task t2 = tl2.get(2);
        assertEquals(" ", t2.getTaskStatus());
    }
}
