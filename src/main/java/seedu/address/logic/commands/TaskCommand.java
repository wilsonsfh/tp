package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import java.util.List;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;

public class TaskCommand extends Command {
    public static final String COMMAND_WORD = "task";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the specified team member.\n"
        + "Parameters: INDEX (must be a positive integer) TASK_DESCRIPTION\n"
        + "Example: " + COMMAND_WORD + " 1 Complete project milestone";

    public static final String MESSAGE_ADD_TASK_SUCCESS = "Added task to member: %1$s";

    private final Index index;
    private final Task task;

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
        Person updatedPerson = personToEdit.addTask(task);

        model.setPerson(personToEdit, updatedPerson);
        return new CommandResult(String.format(MESSAGE_ADD_TASK_SUCCESS, updatedPerson.getName()));
    }
}
