package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;

/**
 * A command to list all the tasks assigned to a particular member.
 */
public class ListMemberTasksCommand extends Command {
    public static final String COMMAND_WORD = "listtasks";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all tasks for the specified person.\n"
            + "Parameters: PERSON_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    private final Index index;

    public ListMemberTasksCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(index.getZeroBased());
        List<Task> tasks = person.getTasks();

        if (tasks.isEmpty()) {
            return new CommandResult(String.format(Messages.MESSAGE_NO_TASK_FOR_MEM, person.getName()));
        }

        StringBuilder taskList = new StringBuilder();
        taskList.append(String.format("Tasks for %s:\n", person.getName()));
        for (int i = 0; i < tasks.size(); i++) {
            taskList.append(String.format("%d. %s\n", i + 1, tasks.get(i)));
        }

        return new CommandResult(taskList.toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ListMemberTasksCommand)) {
            return false;
        }

        ListMemberTasksCommand otherCommand = (ListMemberTasksCommand) other;
        return index.equals(otherCommand.index);
    }
}
