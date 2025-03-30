package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.util.SampleDataUtil;

public class ListMemberTasksCommandTest {
    private Model model = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexWithTasks_success() {
        ListMemberTasksCommand command = new ListMemberTasksCommand(Index.fromOneBased(1));

        assertDoesNotThrow(() -> command.execute(model));
    }

    @Test
    public void execute_validIndexNoTasks_success() throws Exception {
        Person personWithoutTasks = model.getFilteredPersonList().get(5);

        ListMemberTasksCommand command = new ListMemberTasksCommand(Index.fromOneBased(6));
        CommandResult result = command.execute(model);

        String expectedOutput = String.format(Messages.MESSAGE_NO_TASK_FOR_MEM, personWithoutTasks.getName());
        assertEquals(expectedOutput, result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        int outOfBoundIndex = model.getFilteredPersonList().size() + 1;
        ListMemberTasksCommand command = new ListMemberTasksCommand(Index.fromOneBased(outOfBoundIndex));

        assertThrows(CommandException.class,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () -> command.execute(model));
    }

    @Test
    public void equals() {
        ListMemberTasksCommand firstCommand = new ListMemberTasksCommand(Index.fromOneBased(1));
        ListMemberTasksCommand secondCommand = new ListMemberTasksCommand(Index.fromOneBased(2));

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        ListMemberTasksCommand firstCommandCopy = new ListMemberTasksCommand(Index.fromOneBased(1));
        assertTrue(firstCommand.equals(firstCommandCopy));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different person index -> returns false
        assertFalse(firstCommand.equals(secondCommand));
    }
}
