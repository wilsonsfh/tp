package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;

/**
 * Adds a task to a specified team member identified by an index number used in the displayed person list.
 */
public class TaskCommand extends Command {
    public static final String COMMAND_WORD = "task";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the specified team member.\n"
        + "Usage:\n"
        + "  " + COMMAND_WORD + " INDEX task/TASK_DESCRIPTION[, DUE_DATE][, STATUS]\n"
        + "\n"
        + "Parameters:\n"
        + "  INDEX            - A positive integer indicating the person's order in the list.\n"
        + "  task/            - Task description (required).\n"
        + "  DUE_DATE         - (Optional) In the format yyyy-MM-dd HH:mm\n"
        + "  STATUS           - (Optional) One of: 'yet to start', 'in progress', 'completed'\n"
        + "\n"
        + "Examples:\n"
        + "  " + COMMAND_WORD + " 1 task/Submit report, 2025-12-31 23:59, yet to start\n"
        + "  " + COMMAND_WORD + " 2 task/Buy groceries\n"
        + "  " + COMMAND_WORD + " 3 task/Plan meeting, 2025-10-01 09:00";

    public static final String MESSAGE_ADD_TASK_SUCCESS = "Added task to member: %1$s";

    private final Index index;
    private final Task task;

    /**
     * Creates a TaskCommand to add the specified {@code Task} to the team member at the specified {@code Index}.
     * @param index Index of the team member in the displayed person list.
     * @param task Task to be added to the team member.
     */
    public TaskCommand(Index index, Task task) {
        requireNonNull(index);
        requireNonNull(task);
        this.index = index;
        this.task = task;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        List<Task> updatedTasks = new ArrayList<>(personToEdit.getTasks());

        for (Task existingTask : updatedTasks) {
            if (existingTask.getDescription().equals(task.getDescription())) {
                throw new CommandException("This task already exists for the selected person.\n"
                + "Task description cannot be the same!");
            }
        }
        updatedTasks.add(task);

        Person updatedPerson = personToEdit.addTask(task);

        model.setPerson(personToEdit, updatedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_ADD_TASK_SUCCESS, updatedPerson.getName()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TaskCommand)) {
            return false;
        }
        TaskCommand other = (TaskCommand) obj;
        return index.equals(other.index) && task.equals(other.task);
    }
}
