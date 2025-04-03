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
        + "  " + COMMAND_WORD + " PERSON_INDEX TASK_INDEX [desc/NEW_DESCRIPTION] [due/NEW_DUE_DATE] [status/NEW_STATUS]\n"
        + "\n"
        + "Parameters:\n"
        + "  PERSON_INDEX    - A positive integer representing the team member's index in the displayed list.\n"
        + "  TASK_INDEX      - A positive integer representing the task's index in the team member's task list.\n"
        + "  desc/           - (Optional) New task description.\n"
        + "  due/            - (Optional) New due date in the format yyyy-MM-dd HH:mm.\n"
        + "  status/         - (Optional) New task status. One of: 'yet to start', 'in progress', 'completed'.\n"
        + "\n"
        + "Example:\n"
        + "  " + COMMAND_WORD + " 1 2 desc/Submit revised report due/2025-12-31 23:59 status/in progress";
    public static final String MESSAGE_UPDATE_TASK_SUCCESS = "Updated task for %1$s: %2$s";
    public static final String MESSAGE_NOT_UPDATED = "At least one field to update must be provided.";

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
            throw new IllegalArgumentException(MESSAGE_NOT_UPDATED);
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

        if (personIndex.getZeroBased() >= lastShownList.size() || personIndex.getZeroBased() >= tasks.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
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
        return new CommandResult(String.format(MESSAGE_UPDATE_TASK_SUCCESS,
            updatedPerson.getName(), updatedTask.getDescription()));
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
