package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;

/**
 * Updates an existing task for the specified team member.
 */
public class UpdateTaskCommand extends Command {

    public static final String COMMAND_WORD = "updatetask";
    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Updates an existing task for the specified team member.\n"
        + "Usage:\n"
        + "  " + COMMAND_WORD + " PERSON_INDEX TASK_INDEX DESCRIPTION[, DUE_DATE][, STATUS]\n"
        + "\n"
        + "Parameters:\n"
        + "  PERSON_INDEX - A positive integer representing the team member's index in the displayed list.\n"
        + "  TASK_INDEX   - A positive integer representing the task's index in the team member's task list.\n"
        + "  DESCRIPTION  - The new task description.\n"
        + "  DUE_DATE     - (Optional) New due date in format yyyy-MM-dd HH:mm.\n"
        + "  STATUS       - (Optional) New task status: 'yet to start', 'in progress', 'completed'.\n"
        + "\n"
        + "Examples:\n"
        + "  " + COMMAND_WORD + " 1 2 Buy milk\n"
        + "  " + COMMAND_WORD + " 1 2 Submit report, in progress\n"
        + "  " + COMMAND_WORD + " 2 1 Finalize project, 2025-12-31 23:59, completed";



    private final Index personIndex;
    private final Index taskIndex;
    private final Optional<String> newDescription;
    private final Optional<LocalDateTime> newDueDate;
    private final Optional<TaskStatus> newStatus;

    /**
     * Constructs an UpdateTaskCommand.
     *
     * @param personIndex Index of the team member.
     * @param taskIndex Index of the task in the team member's task list.
     * @param newDescription Optional new task description.
     * @param newDueDate Optional new due date.
     * @param newStatus Optional new task status.
     */
    public UpdateTaskCommand(Index personIndex, Index taskIndex, Optional<String> newDescription,
                             Optional<LocalDateTime> newDueDate, Optional<TaskStatus> newStatus) {
        requireNonNull(personIndex);
        requireNonNull(taskIndex);
        requireNonNull(newDescription);
        requireNonNull(newDueDate);
        requireNonNull(newStatus);

        if (!newDescription.isPresent() && !newDueDate.isPresent() && !newStatus.isPresent()) {
            throw new IllegalArgumentException(Messages.MESSAGE_NOT_UPDATED);
        }

        this.personIndex = personIndex;
        this.taskIndex = taskIndex;
        this.newDescription = newDescription;
        this.newDueDate = newDueDate;
        this.newStatus = newStatus;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Person personToEdit = lastShownList.get(personIndex.getZeroBased());
        List<Task> tasks = new ArrayList<>(personToEdit.getTasks());

        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        if (taskIndex.getZeroBased() >= tasks.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToUpdate = tasks.get(taskIndex.getZeroBased());
        Task updatedTask = taskToUpdate;

        if (newDescription.isPresent()) {
            updatedTask = updatedTask.withDescription(newDescription.get());
        }
        if (newDueDate.isPresent()) {
            updatedTask = updatedTask.withDueDate(newDueDate.get());
        }
        if (newStatus.isPresent()) {
            updatedTask = updatedTask.withStatus(newStatus.get());
        }

        tasks.set(taskIndex.getZeroBased(), updatedTask);

        // Create a new Person with the updated tasks list.
        Person updatedPerson = new Person(
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
            tasks
        );

        model.setPerson(personToEdit, updatedPerson);

        String message;
        if (newDescription.isEmpty() && newDueDate.isEmpty() && newStatus.isPresent()) {
            // Status-only case (like `mark`)
            message = String.format("Successfully updated task \"%s\" to status \"%s\" for %s.\n"
                    + "Tip: Use the 'listtasks %d' command to view all tasks for this team member.",
                updatedTask.getDescription(),
                updatedTask.getStatus(),
                updatedPerson.getName(),
                personIndex.getOneBased());
        } else {
            // Generic update case
            message = String.format("Successfully updated task for %s:\n"
                    + "• Description: %s\n"
                    + "• Due Date: %s\n"
                    + "• Status: %s\n"
                    + "Tip: Use the 'listtasks %d' command to view the updated task list.",
                updatedPerson.getName(),
                updatedTask.getDescription(),
                updatedTask.getDueDate() == null ? "None" : updatedTask.getDueDate().toString(),
                updatedTask.getStatus(),
                personIndex.getOneBased());
        }

        return new CommandResult(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UpdateTaskCommand)) {
            return false;
        }
        UpdateTaskCommand other = (UpdateTaskCommand) obj;
        return personIndex.equals(other.personIndex)
            && taskIndex.equals(other.taskIndex)
            && newDescription.equals(other.newDescription)
            && newDueDate.equals(other.newDueDate)
            && newStatus.equals(other.newStatus);
    }
}
