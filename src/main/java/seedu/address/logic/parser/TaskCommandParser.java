package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INCORRECT_DATE_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DUE_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_DESC;
import static seedu.address.logic.parser.ParserUtil.parseDueDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TaskCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;

/**
 * Parses input and creates a new {@code TaskCommand}.
 */
public class TaskCommandParser implements Parser<TaskCommand> {
    private static final Logger logger = Logger.getLogger(TaskCommandParser.class.getName());

    @Override
    public TaskCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_TASK);

        Index index;
        LocalDateTime dueDate = null;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
            logger.info("Parsed index: " + index.getOneBased());
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TaskCommand.MESSAGE_USAGE), pe);
        }

        if (argMultimap.getAllValues(PREFIX_TASK).size() > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TaskCommand.MESSAGE_USAGE));
        }

        // If the task is provided, parse the complete task string.
        if (argMultimap.getValue(PREFIX_TASK).isPresent()) {
            String taskString = argMultimap.getValue(PREFIX_TASK).get();
            Task task = ParserUtil.parseTask(taskString);
            logger.info("Task string: " + taskString);
            return new TaskCommand(index, task);
        } else if (argMultimap.getValue(PREFIX_DUE_DATE).isPresent()) {
            String taskDesc = argMultimap.getValue(PREFIX_TASK_DESC).get();
            try {
                dueDate = parseDueDate(argMultimap);
            } catch (DateTimeParseException e) {
                throw new ParseException(MESSAGE_INCORRECT_DATE_FORMAT);
            }
            // In this mode, default the task status to YET_TO_START.
            Task task = new Task(taskDesc, TaskStatus.YET_TO_START, dueDate);
            return new TaskCommand(index, task);
        } else {
            // If no task is provided, return a command with an empty task,
            // or throw an exception if task input is mandatory.
            return new TaskCommand(index, new Task());
        }
    }
}

