package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

public class TaskCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validTask_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        Task newTask = new Task("Write report", TaskStatus.IN_PROGRESS,
            LocalDateTime.of(2025, 12, 31, 23, 59));

        TaskCommand command = new TaskCommand(targetIndex, newTask);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person originalPerson = expectedModel.getFilteredPersonList().get(targetIndex.getZeroBased());
        List<Task> updatedTasks = new ArrayList<>(originalPerson.getTasks());
        updatedTasks.add(newTask);

        Person updatedPerson = new Person(
            originalPerson.getName(),
            originalPerson.getPhone(),
            originalPerson.getEmail(),
            originalPerson.getTelegram(),
            originalPerson.getPosition(),
            originalPerson.getAddress(),
            originalPerson.getTags(),
            originalPerson.getSkills(),
            originalPerson.getOthers(),
            originalPerson.getTaskStatus(),
            updatedTasks
        );

        String formattedDueDate = newTask.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String expectedMessage = String.format(TaskCommand.MESSAGE_ADD_TASK_SUCCESS,
            updatedPerson.getName(), newTask.getDescription(),
            formattedDueDate, newTask.getStatus(), targetIndex.getOneBased());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateTask_throwsCommandException() {
        Index personIndex = INDEX_FIRST_PERSON;
        Person person = model.getFilteredPersonList().get(personIndex.getZeroBased());
        Task existingTask = person.getTasks().get(0); // assumes at least one task exists

        TaskCommand duplicateCommand = new TaskCommand(personIndex, existingTask);

        assertThrows(CommandException.class, () -> duplicateCommand.execute(model));
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundsIndex = Index.fromOneBased(100); // assumed out of bounds
        Task newTask = new Task("Prepare slides", TaskStatus.YET_TO_START, null);
        TaskCommand command = new TaskCommand(outOfBoundsIndex, newTask);

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void equals_sameContent_returnsTrue() {
        Task task = new Task("Review design", TaskStatus.YET_TO_START, null);
        TaskCommand commandA = new TaskCommand(INDEX_FIRST_PERSON, task);
        TaskCommand commandB = new TaskCommand(INDEX_FIRST_PERSON, task);
        assertTrue(commandA.equals(commandB));
    }

    @Test
    public void equals_differentIndexOrTask_returnsFalse() {
        Task task1 = new Task("Design UI", TaskStatus.YET_TO_START, null);
        Task task2 = new Task("Write unit tests", TaskStatus.COMPLETED,
            LocalDateTime.of(2024, 11, 12, 18, 0));

        TaskCommand command1 = new TaskCommand(INDEX_FIRST_PERSON, task1);
        TaskCommand command2 = new TaskCommand(INDEX_SECOND_PERSON, task1);
        TaskCommand command3 = new TaskCommand(INDEX_FIRST_PERSON, task2);

        assertTrue(!command1.equals(command2));
        assertTrue(!command1.equals(command3));
    }
}
