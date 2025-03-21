package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;

/**
 * Generates a summary report of task completion statuses.
 */
public class ReportCommand extends Command {
    public static final String COMMAND_WORD = "report";
    public static final String MESSAGE_SUCCESS = "Task Status Report Generated!\n"
            + "Total Tasks: %d\n"
            + "Completed: %d\n"
            + "In Progress: %d\n"
            + "Yet to Start: %d";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        List<Person> personList = model.getFilteredPersonList();

        int totalTasks = 0;
        int completed = 0;
        int inProgress = 0;
        int yetToStart = 0;

        for (Person person : personList) {
            for (Task task : person.getTasks()) {
                totalTasks++;
                if (task.getStatus().equals(TaskStatus.COMPLETED)) {
                    completed++;
                } else if (task.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                    inProgress++;
                } else if (task.getStatus().equals(TaskStatus.YET_TO_START)) {
                    yetToStart++;
                }
            }
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, totalTasks, completed, inProgress, yetToStart));
    }
}
