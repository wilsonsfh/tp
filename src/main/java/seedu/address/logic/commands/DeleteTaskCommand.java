package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;

/**
 * Deletes a task from a person identified using their displayed index from the address book.
 * The task to be deleted is identified by its index in the person's task list.
 */
public class DeleteTaskCommand extends Command {
    public static final String COMMAND_WORD = "deltask";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the specified task of the specified person. \n"
            + "Parameters: PERSON_INDEX TASK_INDEX (both must be a positive integer that "
            + "corresponds to member and their task' index)\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Successfully deleted task: %1$s, for member: %2$s";

    private final Logger logger = LogsCenter.getLogger(DeleteTaskCommand.class);

    private final Index personIndex;

    private final Index taskIndex;

    /**
     * Creates a DeleteTaskCommand to delete the specified task from the specified person.
     *
     * @param personIndex The index of the person in the filtered person list.
     * @param taskIndex The index of the task in the person's task list.
     */
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
        logger.info("----------------[TASK INDEX][" + taskInt + "]");

        if (taskInt >= personToEdit.getTasks().size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX,
                    taskIndex.getOneBased()));
        }

        Task taskToRemove = personToEdit.getTasks().get(taskInt);
        Person updatedPerson = personToEdit.removeTask(taskInt);

        model.setPerson(personToEdit, updatedPerson);

        return new CommandResult(String.format(
                MESSAGE_DELETE_TASK_SUCCESS,
                taskToRemove.getDescription(),
                updatedPerson.getName()
        ));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteTaskCommand)) {
            return false;
        }

        DeleteTaskCommand otherCommand = (DeleteTaskCommand) other;
        return personIndex.equals(otherCommand.personIndex)
                && taskIndex.equals(otherCommand.taskIndex);
    }
}
