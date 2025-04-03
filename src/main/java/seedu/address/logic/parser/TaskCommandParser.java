package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_EMPTY_TASK_DESC;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;

import java.util.logging.Logger;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TaskCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.Task;

/**
 * Parses input and creates a new {@code TaskCommand}.
 */
public class TaskCommandParser implements Parser<TaskCommand> {
    private static final Logger logger = Logger.getLogger(TaskCommandParser.class.getName());

    @Override
    public TaskCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TASK);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_TASK);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
            logger.info("Parsed index: " + index.getOneBased());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TaskCommand.MESSAGE_USAGE), pe);
        }

        if (argMultimap.getAllValues(PREFIX_TASK).size() > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TaskCommand.MESSAGE_USAGE));
        }

        String taskString = argMultimap.getValue(PREFIX_TASK).orElse("").trim();
        if (taskString.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_TASK_DESC);
        }

        logger.info("Task string: " + taskString);
        Task task = ParserUtil.parseTask(taskString);
        return new TaskCommand(index, task);
    }
}
