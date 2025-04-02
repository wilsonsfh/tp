package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;
import seedu.address.model.util.SampleDataUtil;

public class DeleteTaskCommandTest {
    private Model model;

    @BeforeEach
    void setUp() {
        // Model from SampleDataUtil's address book
        model = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndices_success() {
        // Get the first person and add a task to them
        Person personWithTask = model.getFilteredPersonList().get(0);
        Task taskToAdd = new Task("Test Task", TaskStatus.YET_TO_START, null);
        Person updatedPerson = personWithTask.addTask(taskToAdd);
        model.setPerson(personWithTask, updatedPerson);

        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(2)); // Delete the second task (added task is 2nd)

        String expectedMessage = String.format(
                DeleteTaskCommand.MESSAGE_DELETE_TASK_SUCCESS,
                taskToAdd.getDescription(),
                updatedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(deleteTaskCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        String expectedMessage = String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX,
                outOfBoundPersonIndex.getOneBased());
        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(
                outOfBoundPersonIndex,
                Index.fromOneBased(1));

        assertCommandFailure(deleteTaskCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidTaskIndex_throwsCommandException() {
        // Get the first person
        Person person = model.getFilteredPersonList().get(0);
        int taskCount = person.getTasks().size();
        Index outOfBoundTaskIndex = Index.fromOneBased(taskCount + 1);

        DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(
                Index.fromOneBased(1),
                outOfBoundTaskIndex);

        String expectedMessage = String.format(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX,
                outOfBoundTaskIndex.getOneBased());

        assertCommandFailure(deleteTaskCommand, model, expectedMessage);
    }
}
