package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_TASK;
import static seedu.address.logic.Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        + "  DESCRIPTION  - The new task description (optional if only updating due date or status).\n"
        + "  DUE_DATE     - (Optional) New due date in format yyyy-MM-dd HH:mm.\n"
        + "  STATUS       - (Optional) New task status: 'yet to start', 'in progress', 'completed'.\n"
        + "\n"
        + "Notes:\n"
        + "  • Use commas (,) to separate multiple fields.\n"
        + "  • Order of parameters matters.\n"
        + "\n"
        + "Examples:\n"
        + "  " + COMMAND_WORD + " 1 2 Buy milk\n"
        + "  " + COMMAND_WORD + " 1 2 Submit report, in progress\n"
        + "  " + COMMAND_WORD + " 2 1 Finalize project, 2025-12-31 23:59, completed\n"
        + "  " + COMMAND_WORD + " 1 1 2025-12-31 23:59\n"
        + "  " + COMMAND_WORD + " 3 2 completed\n"
        + "  " + COMMAND_WORD + " 4 1 2025-12-31 23:59, in progress";

    private static final String TASK_UPDATED_MESSAGE = "Successfully updated task for %s:\n"
        + "• Description: %s\n• Due Date: %s\n• Status: %s\n"
        + "Tip: Use the 'listtasks %d' command to view all tasks for this team member.";

    private final Index personIndex;
    private final Index taskIndex;
    private final Optional<String> newDescription;
    private final Optional<LocalDateTime> newDueDate;
    private final Optional<TaskStatus> newStatus;

    /**
     * Constructs an UpdateTaskCommand.
     */
    public UpdateTaskCommand(Index personIndex, Index taskIndex, Optional<String> newDescription,
                             Optional<LocalDateTime> newDueDate, Optional<TaskStatus> newStatus) {
        requireNonNull(personIndex);
        requireNonNull(taskIndex);
        requireNonNull(newDescription);
        requireNonNull(newDueDate);
        requireNonNull(newStatus);

        if (newDescription.isEmpty() && newDueDate.isEmpty() && newStatus.isEmpty()) {
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
        List<Person> personList = model.getFilteredPersonList();

        Person personToEdit = getPerson(personList);
        List<Task> updatedTasks = new ArrayList<>(personToEdit.getTasks());

        Task updatedTask = getUpdatedTask(updatedTasks);
        checkForDuplicateTaskDescription(updatedTasks);
        updatedTasks.set(taskIndex.getZeroBased(), updatedTask);

        Person updatedPerson = recreatePersonWithTasks(personToEdit, updatedTasks);
        model.setPerson(personToEdit, updatedPerson);

        assert updatedTasks.size() == updatedPerson.getTasks().size();

        return new CommandResult(buildSuccessMessage(updatedPerson, updatedTask));
    }

    private Person getPerson(List<Person> personList) throws CommandException {
        if (personIndex.getZeroBased() >= personList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return personList.get(personIndex.getZeroBased());
    }

    private Task getUpdatedTask(List<Task> tasks) throws CommandException {
        if (taskIndex.getZeroBased() >= tasks.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_TASK_DISPLAYED_INDEX, taskIndex.getOneBased()));
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

        return updatedTask;
    }

    private void checkForDuplicateTaskDescription(List<Task> tasks) throws CommandException {
        for (int i = 0; i < tasks.size(); i++) {
            if (i == taskIndex.getZeroBased()) {
                continue;
            }
            if (newDescription.isPresent()
                && tasks.get(i).getDescription().equals(newDescription.get())) {
                throw new CommandException(MESSAGE_DUPLICATE_TASK);
            }
        }
    }

    private Person recreatePersonWithTasks(Person person, List<Task> updatedTasks) {
        return new Person(
            person.getName(),
            person.getPhone(),
            person.getEmail(),
            person.getTelegram(),
            person.getPosition(),
            person.getAddress(),
            person.getTags(),
            person.getSkills(),
            person.getOthers(),
            person.getTaskStatus(),
            updatedTasks
        );
    }

    private String buildSuccessMessage(Person updatedPerson, Task updatedTask) {
        String formattedDueDate = Optional.ofNullable(updatedTask.getDueDate())
            .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
            .orElse("No due date set");

        return String.format(TASK_UPDATED_MESSAGE,
            updatedPerson.getName(),
            updatedTask.getDescription(),
            formattedDueDate,
            updatedTask.getStatus(),
            personIndex.getOneBased());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UpdateTaskCommand)) {
            return false;
        }
        UpdateTaskCommand otherCommand = (UpdateTaskCommand) other;
        return personIndex.equals(otherCommand.personIndex)
            && taskIndex.equals(otherCommand.taskIndex)
            && newDescription.equals(otherCommand.newDescription)
            && newDueDate.equals(otherCommand.newDueDate)
            && newStatus.equals(otherCommand.newStatus);
    }
}
