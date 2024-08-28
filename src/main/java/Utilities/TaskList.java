package utilities;

import java.util.ArrayList;

import tasks.Task;

public class TaskList {
    private ArrayList<Task> taskList;

    public TaskList(Storage s) {
        this.taskList = s.loadTaskListFromFile();
    }

    /**
     * Adds new task into the TaskList.
     *
     * @param t Task to be added.
     * @param name Name of the task to be added.
     */
    public void addToTaskList(Task t, String name) {
        this.taskList.add(t);
        UI.updateUserOnAddition(name, this.taskList.size());
    }

    /**
     * Removes task from the TaskList.
     *
     * @param index Index of task to be removed.
     */
    public void removeFromTaskList(int index) {
        Task t = this.taskList.get(index);
        this.taskList.remove(index);
        UI.updateUserOnDeletion(t);
    }

    /**
     * Updates task status in the TaskList.
     *
     * @param index Index of task to be updated.
     * @param status Boolean status to update.
     */
    public void updateTaskListStatus(int index, boolean status) {
        Task t = this.taskList.get(index);
        if (status) {
            t.markAsDone();
            UI.updateUserOnCompletion(t);
        } else {
            t.markAsNotDone();
            UI.updateUserOnUncompletion(t);
        }
    }

    /**
     * Finds and prints matching tasks for given keyword.
     *
     * @param input String pattern to be matched for search.
     */
    public void findTasks(String input) {
        System.out.println("I found some matching tasks in your list:");
        int count = 1;
        for (Task t : this.taskList) {
            if (t.toString().contains(input)) {
                System.out.println(String.format("%d.%s", count, t.toString()));
                count++;
            }
        }
    }

    public int getSize() {
        return this.taskList.size();
    }

    @Override
    public String toString() {
        String output = "";
        int count = 1;
        for (Task t : this.taskList) {
            String temp = String.format("%d.%s \n", count, t.toString());
            output += temp;
            count++;
        }

        return output;
    }
}
