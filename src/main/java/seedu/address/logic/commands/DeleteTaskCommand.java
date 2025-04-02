package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;

public class DeleteTaskCommand extends Command {
    public static final String COMMAND_WORD = "deltask";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the specified task of the specified person. \n"
            + "Parameters: PERSON_INDEX TASK_INDEX (both must be a positive integer that "
            + "corresponds to member and their task' index)\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Successfully deleted task: %1$s, for member: %2$s";

    private final Index personIndex;

    private final Index taskIndex;

    public DeleteTaskCommand(Index personIndex, Index taskIndex) {
        requireNonNull(personIndex);
        requireNonNull(taskIndex);
        this.personIndex = personIndex;
        this.taskIndex = taskIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(personIndex.getZeroBased());
        int taskInt = taskIndex.getZeroBased();
        Task taskToRemove = personToEdit.getTasks().get(taskInt);

        if (taskInt >= personToEdit.getTasks().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Person updatedPerson = personToEdit.removeTask(taskInt);

        model.setPerson(personToEdit, updatedPerson);

        return new CommandResult(String.format(
                MESSAGE_DELETE_TASK_SUCCESS,
                taskToRemove.getDescription(),
                updatedPerson.getName()
        ));
    }
}
