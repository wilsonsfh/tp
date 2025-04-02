package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteTaskCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class DeleteTaskCommandParserTest {

    private final DeleteTaskCommandParser parser = new DeleteTaskCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteTaskCommand() throws ParseException {
        String userInput = "1 2";
        DeleteTaskCommand expectedCommand = new DeleteTaskCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(2));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidIndexArgs_throwsParseException() {
        String expectedMessage = MESSAGE_INVALID_INDEX;

        // Non-numeric arguments
        assertParseFailure(parser, "a b", expectedMessage);
        assertParseFailure(parser, "1 b", expectedMessage);
        assertParseFailure(parser, "a 1", expectedMessage);

        // Zero or negative indices
        assertParseFailure(parser, "0 1", expectedMessage);
        assertParseFailure(parser, "1 0", expectedMessage);
        assertParseFailure(parser, "-1 1", expectedMessage);
        assertParseFailure(parser, "1 -1", expectedMessage);
    }

    @Test
    public void parse_extraWhitespace_handledCorrectly() throws ParseException {
        String userInput = "   1     2   ";
        DeleteTaskCommand expectedCommand = new DeleteTaskCommand(
                Index.fromOneBased(1),
                Index.fromOneBased(2));
        assertEquals(expectedCommand, parser.parse(userInput));
    }

    @Test
    public void parse_invalidFormat_throwsParseExceptionWithCorrectMessage() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTaskCommand.MESSAGE_USAGE);

        // Test with empty string
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "   ", expectedMessage);

        // Test with single argument
        assertParseFailure(parser, "1", expectedMessage);
        assertParseFailure(parser, "   1  ", expectedMessage);

        // Test with three arguments
        assertParseFailure(parser, "1 2 3", expectedMessage);
    }
}
