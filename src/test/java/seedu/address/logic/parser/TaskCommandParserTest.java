package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_EMPTY_TASK_DESC;
import static seedu.address.logic.Messages.MESSAGE_INCORRECT_DATE_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INCORRECT_TASK_STATUS;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_TASK_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TaskCommand;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;

public class TaskCommandParserTest {

    private TaskCommandParser parser = new TaskCommandParser();

    @Test
    public void parse_validCompleteTask_success() {
        String userInput = "2 task/Buy groceries, 2025-12-31 23:59, yet to start";
        Task expectedTask = new Task("Buy groceries", TaskStatus.YET_TO_START,
            LocalDateTime.of(2025, 12, 31, 23, 59));
        TaskCommand expectedCommand = new TaskCommand(Index.fromOneBased(2), expectedTask);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_onlyDescription_success() {
        String userInput = "1 task/Submit report";
        Task expectedTask = new Task("Submit report", TaskStatus.YET_TO_START, null);
        TaskCommand expectedCommand = new TaskCommand(Index.fromOneBased(1), expectedTask);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_descriptionAndDueDate_success() {
        String userInput = "1 task/Submit report, 2025-12-31 23:59";
        Task expectedTask = new Task("Submit report", TaskStatus.YET_TO_START,
            LocalDateTime.of(2025, 12, 31, 23, 59));
        TaskCommand expectedCommand = new TaskCommand(Index.fromOneBased(1), expectedTask);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_descriptionAndStatus_success() {
        String userInput = "1 task/Submit report, completed";
        Task expectedTask = new Task("Submit report", TaskStatus.COMPLETED, null);
        TaskCommand expectedCommand = new TaskCommand(Index.fromOneBased(1), expectedTask);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingIndex_failure() {
        String userInput = "task/Do homework, 2025-12-31 23:59, yet to start";
        assertParseFailure(parser, userInput,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, TaskCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_failure() {
        String userInput = "zero task/Do homework, 2025-12-31 23:59, yet to start";
        assertParseFailure(parser, userInput,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, TaskCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDateFormat_failure() {
        String userInput = "1 task/Do homework, 31-12-2025 23:59, yet to start";
        assertParseFailure(parser, userInput, MESSAGE_INCORRECT_DATE_FORMAT.trim());
    }

    @Test
    public void parse_invalidTaskStatus_failure() {
        String userInput = "1 task/Submit report, 2025-12-31 23:59, done";
        assertParseFailure(parser, userInput, MESSAGE_INCORRECT_TASK_STATUS);
    }

    @Test
    public void parse_tooManyFields_failure() {
        String userInput = "1 task/Submit report, 2025-12-31 23:59, completed, extra";
        assertParseFailure(parser, userInput, MESSAGE_INVALID_TASK_FORMAT);
    }

    @Test
    public void parse_emptyTask_failure() {
        String userInput = "2 task/";
        assertParseFailure(parser, userInput, MESSAGE_EMPTY_TASK_DESC);
    }

    @Test
    public void parse_blankDescription_failure() {
        String userInput = "1 task/   ";
        assertParseFailure(parser, userInput, MESSAGE_EMPTY_TASK_DESC);
    }

    @Test
    public void parse_blankFields_failure() {
        String userInput = "1 task/   , 2025-12-31 23:59, yet to start";
        assertParseFailure(parser, userInput, MESSAGE_EMPTY_TASK_DESC);
    }

}
