package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

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

/**
 * Adds a task to a specified team member identified by an index number used in the displayed person list.
 */
public class TaskCommand extends Command {
    public static final String COMMAND_WORD = "task";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the specified team member.\n"
        + "Usage:\n"
        + "  " + COMMAND_WORD + " INDEX task/TASK_DESCRIPTION[, DUE_DATE][, STATUS]\n\n"
        + "Parameters:\n"
        + "  INDEX            - A positive integer indicating the person's order in the list.\n"
        + "  task/            - Task description (required).\n"
        + "  DUE_DATE         - (Optional) Format: yyyy-MM-dd HH:mm\n"
        + "  STATUS           - (Optional) 'yet to start', 'in progress', 'completed'\n\n"
        + "Examples:\n"
        + "  " + COMMAND_WORD + " 1 task/Submit report, 2025-12-31 23:59, yet to start\n"
        + "  " + COMMAND_WORD + " 2 task/Buy groceries";

    public static final String MESSAGE_ADD_TASK_SUCCESS = "Successfully added task to %1$s:\n"
        + "• Description: %2$s\n"
        + "• Due Date: %3$s\n"
        + "• Status: %4$s\n"
        + "Tip: Use the 'listtasks %5$d' command to view all tasks for this team member.";

    private static final String DUPLICATE_TASK_ERROR = "This task already exists for the selected person.\n"
        + "Task description cannot be the same!";

    private final Index index;
    private final Task task;

    /**
     * Stores
     * @param index
     * @param task
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
        Person personToEdit = getPersonFromList(model);
        List<Task> currentTasks = new ArrayList<>(personToEdit.getTasks());

        checkForDuplicateTask(currentTasks);
        currentTasks.add(task);

        Person updatedPerson = createUpdatedPerson(personToEdit, currentTasks);
        model.setPerson(personToEdit, updatedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(formatSuccessMessage(updatedPerson));
    }

    private Person getPersonFromList(Model model) throws CommandException {
        List<Person> personList = model.getFilteredPersonList();
        if (index.getZeroBased() >= personList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        return personList.get(index.getZeroBased());
    }

    private void checkForDuplicateTask(List<Task> tasks) throws CommandException {
        for (Task existingTask : tasks) {
            if (existingTask.getDescription().equals(task.getDescription())) {
                throw new CommandException(DUPLICATE_TASK_ERROR);
            }
        }
    }

    private Person createUpdatedPerson(Person original, List<Task> updatedTasks) {
        return new Person(
            original.getName(),
            original.getPhone(),
            original.getEmail(),
            original.getTelegram(),
            original.getPosition(),
            original.getAddress(),
            original.getTags(),
            original.getSkills(),
            original.getOthers(),
            original.getTaskStatus(),
            updatedTasks
        );
    }

    private String formatSuccessMessage(Person updatedPerson) {
        String formattedDueDate = Optional.ofNullable(task.getDueDate())
            .map(d -> d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
            .orElse("No due date set");

        return String.format(MESSAGE_ADD_TASK_SUCCESS,
            updatedPerson.getName(),
            task.getDescription(),
            formattedDueDate,
            task.getStatus().toString(),
            index.getOneBased());
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
