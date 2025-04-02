package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ListMemberTasksCommand;

public class ListMemberTasksCommandParserTest {
    private final ListMemberTasksCommandParser parser = new ListMemberTasksCommandParser();
    private String failureMessage = String.format("Invalid input: %s\n%s", MESSAGE_INVALID_INDEX,
            ListMemberTasksCommand.MESSAGE_USAGE);

    @Test
    public void parse_validArgs_returnsListMemberTasksCommand() {
        // Valid index
        assertParseSuccess(parser, "1", new ListMemberTasksCommand(Index.fromOneBased(1)));

        // Valid index with whitespace
        assertParseSuccess(parser, "   2   ", new ListMemberTasksCommand(Index.fromOneBased(2)));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Empty input
        assertParseFailure(parser, "", failureMessage);

        // Non-integer input
        assertParseFailure(parser, "a", failureMessage);

        // Zero index
        assertParseFailure(parser, "0", failureMessage);

        // Negative index
        assertParseFailure(parser, "-1", failureMessage);

        // Multiple indices
        assertParseFailure(parser, "1 2 3", failureMessage);
    }
}
