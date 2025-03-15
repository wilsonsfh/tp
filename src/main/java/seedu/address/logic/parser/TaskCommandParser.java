package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TaskCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;

/**
 * Parses input and creates a new {@code TaskCommand}.
 */
public class TaskCommandParser implements Parser<TaskCommand> {
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<index>\\d+)\\s+(?<taskDesc>.+)");

    @Override
    public TaskCommand parse(String args) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TaskCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(matcher.group("index"));
        String taskDescription = matcher.group("taskDesc");

        return new TaskCommand(index, new Task(taskDescription, TaskStatus.YET_TO_START));
    }
}

