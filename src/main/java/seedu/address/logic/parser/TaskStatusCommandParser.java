package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TaskStatusCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.TaskStatus;

/**
 * Parses input for TaskStatusCommand.
 */
public class TaskStatusCommandParser implements Parser<TaskStatusCommand> {

    @Override
    public TaskStatusCommand parse(String args) throws ParseException {
        String[] parts = args.trim().split("\\s+");
        if (parts.length < 3) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TaskStatusCommand.MESSAGE_USAGE));
        }

        Index personIndex = ParserUtil.parseIndex(parts[0]);
        Index taskIndex = ParserUtil.parseIndex(parts[1]);
        TaskStatus status;

        String statusString = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length)).trim();

        try {
            status = TaskStatus.valueOf(statusString.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid status provided.\n"
                + "Use one of the following (case-insensitive):\n"
                + "  • completed\n"
                + "  • in progress\n"
                + "  • yet to start\n"
                + "\nExample: mark 1 2 completed");

        }

        return new TaskStatusCommand(personIndex, taskIndex, status);
    }
}
