package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteTaskCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input that aims to delete a task and returns a DeleteTaskCommand.
 */
public class DeleteTaskCommandParser implements Parser<DeleteTaskCommand> {
    @Override
    public DeleteTaskCommand parse(String userInput) throws ParseException {
        String[] parts = userInput.trim().split("\\s+");
        if (parts.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTaskCommand.MESSAGE_USAGE));
        }

        Index personIndex = ParserUtil.parseIndex(parts[0]);
        Index taskIndex = ParserUtil.parseIndex(parts[1]);

        return new DeleteTaskCommand(personIndex, taskIndex);
    }
}
