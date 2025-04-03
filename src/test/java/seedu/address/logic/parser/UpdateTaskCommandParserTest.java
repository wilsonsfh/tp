package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UpdateTaskCommand;
import seedu.address.model.task.TaskStatus;



public class UpdateTaskCommandParserTest {

    private final UpdateTaskCommandParser parser = new UpdateTaskCommandParser();

    @Test
    public void parse_statusOnly_success() throws Exception {
        String input = "1 2 completed";
        UpdateTaskCommand expected = new UpdateTaskCommand(
            Index.fromOneBased(1),
            Index.fromOneBased(2),
            Optional.empty(),
            Optional.empty(),
            Optional.of(TaskStatus.COMPLETED));
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_dueDateOnly_success() throws Exception {
        String input = "1 2 2025-12-31 23:59";
        UpdateTaskCommand expected = new UpdateTaskCommand(
            Index.fromOneBased(1),
            Index.fromOneBased(2),
            Optional.empty(),
            Optional.of(LocalDateTime.of(2025, 12, 31, 23, 59)),
            Optional.empty());
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_descOnly_success() throws Exception {
        String input = "1 2 Write report";
        UpdateTaskCommand expected = new UpdateTaskCommand(
            Index.fromOneBased(1),
            Index.fromOneBased(2),
            Optional.of("Write report"),
            Optional.empty(),
            Optional.empty());
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_descriptionAndStatus_success() throws Exception {
        String input = "1 2 Finish assignment, in progress";
        UpdateTaskCommand expected = new UpdateTaskCommand(
            Index.fromOneBased(1),
            Index.fromOneBased(2),
            Optional.of("Finish assignment"),
            Optional.empty(),
            Optional.of(TaskStatus.IN_PROGRESS));
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_descriptionAndDueDate_success() throws Exception {
        String input = "1 2 Finish assignment, 2025-12-31 23:59";
        UpdateTaskCommand expected = new UpdateTaskCommand(
            Index.fromOneBased(1),
            Index.fromOneBased(2),
            Optional.of("Finish assignment"),
            Optional.of(LocalDateTime.of(2025, 12, 31, 23, 59)),
            Optional.empty());
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_dueDateAndStatus_success() throws Exception {
        String input = "1 2 2025-12-31 23:59, in progress";
        UpdateTaskCommand expected = new UpdateTaskCommand(
            Index.fromOneBased(1),
            Index.fromOneBased(2),
            Optional.empty(),
            Optional.of(LocalDateTime.of(2025, 12, 31, 23, 59)), // Due date parsed
            Optional.of(TaskStatus.IN_PROGRESS)
        );
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_validCompleteTask_success() throws Exception {
        String input = "1 2 Submit final report, 2025-12-31 23:59, completed";
        UpdateTaskCommand expected = new UpdateTaskCommand(
            Index.fromOneBased(1),
            Index.fromOneBased(2),
            Optional.of("Submit final report"),
            Optional.of(LocalDateTime.of(2025, 12, 31, 23, 59)),
            Optional.of(TaskStatus.COMPLETED));
        assertParseSuccess(parser, input, expected);
    }

    @Test
    public void parse_blankDescription_success() throws Exception {
        String input = "1 2 , 2025-12-31 23:59, completed";
        UpdateTaskCommand expected = new UpdateTaskCommand(
            Index.fromOneBased(1),
            Index.fromOneBased(2),
            Optional.empty(), //Do not update task description when input blank
            Optional.of(LocalDateTime.of(2025, 12, 31, 23, 59)),
            Optional.of(TaskStatus.COMPLETED)
        );
        assertParseSuccess(parser, input, expected);
    }


    @Test
    public void parse_nonIntegerIndexes_failure() {
        assertParseFailure(parser, "a b task", "Index is not an integer greater zero.");

    }

    @Test
    public void parse_negativeIndex_failure() {
        assertParseFailure(parser, "-1 1 task", "Index is not an integer greater zero.");

    }

    @Test
    public void parse_zeroIndex_failure() {
        assertParseFailure(parser, "-1 1 task", "Index is not an integer greater zero.");
    }

    @Test
    public void parse_invalidDateFormat_failure() {
        assertParseFailure(parser, "1 2 Write report, 31-12-2025 23:59",
            "Second parameter must be a valid date (yyyy-MM-dd HH:mm) or task status.");
    }

    @Test
    public void parse_invalidStatus_failure() {
        assertParseFailure(parser, "1 2 Write report, 2025-12-31 23:59, done",
            "Third parameter must be a valid task status: 'yet to start', 'in progress', 'completed'.");
    }

    @Test
    public void parse_invalidFieldCount_failure() {
        assertParseFailure(parser, "1 2 A, B, C, D",
            "Too many fields. Use: DESCRIPTION[, DUE_DATE][, STATUS]");
    }

    @Test
    public void parse_statusUsedAsDescription_failure() {
        assertParseFailure(parser, "1 2 completed, 2025-12-31 23:59",
            "Cannot update other fields when only updating task status. Use only: TASK_STATUS");
    }
}
