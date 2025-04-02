package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
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
    public static final String MESSAGE_SUCCESS = "Task Status Report Generated!";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        List<Person> personList = model.getFilteredPersonList();

        List<Person> completedTasks = new ArrayList<>();
        List<Person> inProgressTasks = new ArrayList<>();
        List<Person> yetToStartTasks = new ArrayList<>();

        // For each person, check if they have tasks in each category.
        for (Person person : personList) {
            boolean hasCompleted = false;
            boolean hasInProgress = false;
            boolean hasYetToStart = false;

            for (Task task : person.getTasks()) {
                if (task.getStatus().equals(TaskStatus.COMPLETED)) {
                    hasCompleted = true;
                } else if (task.getStatus().equals(TaskStatus.IN_PROGRESS)) {
                    hasInProgress = true;
                } else if (task.getStatus().equals(TaskStatus.YET_TO_START)) {
                    hasYetToStart = true;
                }
            }
            if (hasCompleted) {
                completedTasks.add(person);
            }
            if (hasInProgress) {
                inProgressTasks.add(person);
            }
            if (hasYetToStart) {
                yetToStartTasks.add(person);
            }
        }
        return new CommandResult(MESSAGE_SUCCESS, completedTasks, inProgressTasks, yetToStartTasks);
    }
}
