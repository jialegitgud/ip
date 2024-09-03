package utilities;

import exceptions.BadDescriptionException;
import exceptions.DateTimeException;
import exceptions.UnknownCommandException;

import java.time.format.DateTimeParseException;

import tasks.Deadlines;
import tasks.Event;
import tasks.Task;
import tasks.TaskTypes;
import tasks.ToDos;

public class CommandParser {
    /**
     * Parses and performs actions for the given command.
     * Returns false only if the "bye" command is given.
     *
     * @param input String command for processing.
     * @param tl TaskList class for maintaining tasks.
     * @param store Storage class for writing to files.
     * @return Boolean value for termination of program.
     */
    public static String parseCommand(String input, TaskList tl, Storage store) {
        String response = "";
        if (input.startsWith("list")) {
            response += "Here are the tasks in your list: ";
            response += tl.toString();
        } else if (input.startsWith("unmark") || input.startsWith("mark")) {
            // extract integer value
            String intValue = input.replaceAll("[^0-9]", "");
            int index = Integer.parseInt(intValue) - 1;
            response += tl.updateTaskListStatus(index, input.startsWith("mark"));
            store.updateFileStatus(index, input.startsWith("mark"));
        } else if (input.startsWith("delete")) {
            // extract integer value
            String intValue = input.replaceAll("[^0-9]", "");
            int index = Integer.parseInt(intValue) - 1;
            response += tl.removeFromTaskList(index);
            store.removeFileTask(index);
        } else if (input.startsWith("find")) {
            String matchValue = input.replace("find", "").strip();
            response += tl.findTasks(matchValue);
        } else {
            try {
                String name = "";
                if (input.startsWith("todo")) {
                    name = input.substring(4).strip();
                    if (name.isEmpty()) {
                        throw new BadDescriptionException(TaskTypes.TODO);
                    }
                    Task t = new ToDos(name);
                    response += tl.addToTaskList(t, name);
                    store.updateFileTasks(String.format("T, %d, %s", 0, name));
                } else if (input.startsWith("deadline")) {
                    String[] splits = input.split("/");
                    name = splits[0].substring(8).strip();
                    if (splits.length != 2 || name.isEmpty()) {
                        throw new BadDescriptionException(TaskTypes.DEADLINE);
                    }
                    // specific to deadlines
                    String details = splits[1].replace("by", "");
                    try {
                        Task t = new Deadlines(name, details.strip());
                        response += tl.addToTaskList(t, name);
                        store.updateFileTasks(String.format("D, %d, %s, %s", 0, name, t.getWriteTaskInfo()));
                    } catch (DateTimeParseException ex) {
                        throw new DateTimeException(name);
                    }
                } else if (input.startsWith("event")) {
                    String[] splits = input.split("/");
                    name = splits[0].substring(5).strip();
                    if (splits.length != 3 || name.isEmpty()) {
                        throw new BadDescriptionException(TaskTypes.EVENT);
                    }
                    // specific to events
                    String startDetails = splits[1].replace("from", "");
                    String endDetails = splits[2].replace("to", "");
                    try {
                        Task t = new Event(name, startDetails.strip(), endDetails.strip());
                        response += tl.addToTaskList(t, name);
                        store.updateFileTasks(String.format("E, %d, %s, %s", 0, name, t.getWriteTaskInfo()));
                    } catch (DateTimeParseException ex) {
                        throw new DateTimeException(name);
                    }
                } else {
                    throw new UnknownCommandException();
                }
            } catch (UnknownCommandException | BadDescriptionException | DateTimeException e) {
                response += UI.updateUserOnError(e);
            }
        }

        return response;
    }
}
