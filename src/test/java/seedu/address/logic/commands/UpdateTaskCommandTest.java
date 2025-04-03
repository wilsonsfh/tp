package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;
import seedu.address.model.util.SampleDataUtil;

public class UpdateTaskCommandTest {

    private Model model;
    private LocalDateTime newDueDate;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
        newDueDate = LocalDateTime.parse("2027-01-01T23:59");
    }

    @Test
    public void execute_validUpdate_success() {
        Index personIndex = INDEX_SECOND_PERSON;
        Index taskIndex = INDEX_FIRST_TASK;

        Task originalTask = model.getFilteredPersonList()
            .get(personIndex.getZeroBased()).getTasks().get(taskIndex.getZeroBased());

        Task expectedTask = new Task(originalTask.getDescription(), TaskStatus.COMPLETED, newDueDate);
        UpdateTaskCommand command = new UpdateTaskCommand(personIndex, taskIndex,
            Optional.empty(), Optional.of(newDueDate), Optional.of(TaskStatus.COMPLETED));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person personToEdit = expectedModel.getFilteredPersonList().get(personIndex.getZeroBased());
        List<Task> updatedTasks = new ArrayList<>(personToEdit.getTasks());

        updatedTasks.set(taskIndex.getZeroBased(), expectedTask);

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

        String expectedMessage = String.format(
            "Successfully updated task for %s:\n• Description: %s\n• Due Date: %s\n• Status: %s\n"
                + "Tip: Use the 'listtasks %d' command to view all tasks for this team member.",
            editedPerson.getName(), expectedTask.getDescription(),
            newDueDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            expectedTask.getStatus(), personIndex.getOneBased());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundsPersonIndex = Index.fromZeroBased(100);
        UpdateTaskCommand command = new UpdateTaskCommand(outOfBoundsPersonIndex, INDEX_FIRST_TASK,
            Optional.of("Update report"), Optional.empty(), Optional.empty());

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_invalidTaskIndex_throwsCommandException() {
        Index outOfBoundsTaskIndex = Index.fromZeroBased(100);
        UpdateTaskCommand command = new UpdateTaskCommand(INDEX_FIRST_PERSON, outOfBoundsTaskIndex,
            Optional.of("Update report"), Optional.empty(), Optional.empty());

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        UpdateTaskCommand command1 = new UpdateTaskCommand(
            INDEX_FIRST_PERSON, INDEX_FIRST_TASK,
            Optional.of("Review code"), Optional.of(newDueDate), Optional.of(TaskStatus.COMPLETED));

        UpdateTaskCommand command2 = new UpdateTaskCommand(
            INDEX_FIRST_PERSON, INDEX_FIRST_TASK,
            Optional.of("Review code"), Optional.of(newDueDate), Optional.of(TaskStatus.COMPLETED));

        assertTrue(command1.equals(command2));
    }
}
