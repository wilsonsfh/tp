package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DUE_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_INDEX;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;

/**
 * Set the due date for a task.
 */
public class SetDueDateCommand extends Command {

    public static final String COMMAND_WORD = "setduedate";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Set a due date for the task under a member.\n"
            + "Parameters: PERSON INDEX (must be a positive integer)\n"
            + PREFIX_TASK_INDEX + "TASK INDEX (must be a positive integer)\n"
            + PREFIX_DUE_DATE + "DUE DATE (yyyy-mm-dd HH:mm)" + "\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_TASK_INDEX + " 1 "
            + PREFIX_DUE_DATE + "2025/01/01 23:59";

    public static final String MESSAGE_SUCCESS_SET_DUE_DATE = "Task due date updated! Person: %1$s";

    private final LocalDateTime dueDate;
    private final Index taskIndex;
    private final Index personIndex;

    /**
     * Creates a SetDueDateCommand to set a due date for the specified task.
     *
     * @param dueDate The new due date for the particular task.
     * @param taskIndex The index of the particular task.
     * @param personIndex The index of the person to whom this task belongs.
     */
    public SetDueDateCommand(LocalDateTime dueDate, Index taskIndex, Index personIndex) {
        this.dueDate = dueDate;
        this.taskIndex = taskIndex;
        this.personIndex = personIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(personIndex.getZeroBased());
        List<Task> updatedTasks = new ArrayList<>(personToEdit.getTasks());

        if (taskIndex.getZeroBased() >= updatedTasks.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX,
                    taskIndex.getOneBased()));
        }

        // Update the due date for the specified task.
        Task taskToUpdate = updatedTasks.get(taskIndex.getZeroBased());
        if (taskToUpdate.getDueDate().equals(dueDate)) {
            throw new CommandException(String.format("Your due date is already: %s", dueDate));
        }
        taskToUpdate.setDueDate(dueDate);

        // Create a new Person with the updated tasks.
        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTelegram(),
                personToEdit.getPosition(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                personToEdit.getSkills(),
                personToEdit.getOthers(),
                personToEdit.getTaskStatus(),
                updatedTasks
        );

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS_SET_DUE_DATE, Messages.format(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SetDueDateCommand)) {
            return false;
        }

        SetDueDateCommand otherCommand = (SetDueDateCommand) other;
        return taskIndex.equals(otherCommand.taskIndex)
                && personIndex.equals(otherCommand.personIndex)
                && dueDate.equals(otherCommand.dueDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", personIndex)
                .add("taskIndex", taskIndex)
                .add("dueDate", dueDate)
                .toString();
    }
}
