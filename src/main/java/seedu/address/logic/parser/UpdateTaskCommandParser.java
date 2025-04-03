package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.time.LocalDateTime;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.UpdateTaskCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.task.TaskStatus;

/**
 * Parses input arguments and creates an UpdateTaskCommand object.
 */
public class UpdateTaskCommandParser implements Parser<UpdateTaskCommand> {

    private static final String ERROR_TOO_MANY_FIELDS = "Too many fields. Use: DESCRIPTION[, DUE_DATE][, STATUS]";
    private static final String ERROR_INVALID_TWO_FIELDS = "Second parameter must be a valid date (yyyy-MM-dd HH:mm) "
        + "or task status.";
    private static final String ERROR_INVALID_STATUS_ONLY_COMBO =
        "Cannot update other fields when only updating task status. Use only: STATUS";
    private static final String ERROR_MISSING_DESCRIPTION =
        "Description cannot be empty when providing multiple fields.";
    private static final String ERROR_INVALID_SECOND_DATE =
        "Second parameter must be a valid date (yyyy-MM-dd HH:mm).";
    private static final String ERROR_INVALID_THIRD_STATUS =
        "Third parameter must be a valid task status: 'yet to start', 'in progress', 'completed'.";

    @Override
    public UpdateTaskCommand parse(String args) throws ParseException {
        String[] parts = args.trim().split("\\s+", 3);
        if (parts.length < 3) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateTaskCommand.MESSAGE_USAGE));
        }

        Index personIndex = ParserUtil.parseIndex(parts[0]);
        Index taskIndex = ParserUtil.parseIndex(parts[1]);
        String[] fields = parts[2].split(",", -1);

        if (fields.length > 3) {
            throw new ParseException(ERROR_TOO_MANY_FIELDS);
        }

        Optional<String> description = Optional.empty();
        Optional<LocalDateTime> dueDate = Optional.empty();
        Optional<TaskStatus> status = Optional.empty();

        String first = fields[0].trim();

        if (fields.length == 1) {
            return handleSingleField(first, personIndex, taskIndex);
        } else if (fields.length == 2) {
            return handleTwoFields(fields[0].trim(), fields[1].trim(), personIndex, taskIndex);
        } else {
            return handleThreeFields(fields[0].trim(), fields[1].trim(), fields[2].trim(), personIndex, taskIndex);
        }
    }

    private UpdateTaskCommand handleSingleField(String first, Index personIndex, Index taskIndex) throws ParseException {
        Optional<String> description = Optional.empty();
        Optional<LocalDateTime> dueDate = Optional.empty();
        Optional<TaskStatus> status = Optional.empty();

        if (TaskStatus.isValidStatus(first)) {
            status = Optional.of(TaskStatus.fromString(first));
        } else {
            try {
                dueDate = Optional.of(ParserUtil.parseAndValidateDueDate(first));
            } catch (ParseException e) {
                description = Optional.of(first);
            }
        }

        ensureAtLeastOneField(description, dueDate, status);
        return new UpdateTaskCommand(personIndex, taskIndex, description, dueDate, status);
    }

    private UpdateTaskCommand handleTwoFields(String first, String second, Index personIndex, Index taskIndex)
        throws ParseException {
        Optional<String> description = Optional.empty();
        Optional<LocalDateTime> dueDate = Optional.empty();
        Optional<TaskStatus> status = Optional.empty();

        if (TaskStatus.isValidStatus(first)) {
            throw new ParseException(ERROR_INVALID_STATUS_ONLY_COMBO);
        }

        try {
            dueDate = Optional.of(ParserUtil.parseAndValidateDueDate(first));
            if (TaskStatus.isValidStatus(second)) {
                status = Optional.of(TaskStatus.fromString(second));
            } else {
                throw new ParseException("Second parameter must be a valid task status when first is a due date.");
            }
        } catch (ParseException e) {
            description = Optional.of(first);
            try {
                dueDate = Optional.of(ParserUtil.parseAndValidateDueDate(second));
            } catch (ParseException ex) {
                if (TaskStatus.isValidStatus(second)) {
                    status = Optional.of(TaskStatus.fromString(second));
                } else {
                    throw new ParseException(ERROR_INVALID_TWO_FIELDS);
                }
            }
        }

        ensureAtLeastOneField(description, dueDate, status);
        return new UpdateTaskCommand(personIndex, taskIndex, description, dueDate, status);
    }

    private UpdateTaskCommand handleThreeFields(String first, String second, String third,
                                                Index personIndex, Index taskIndex) throws ParseException {
        if (first.isEmpty()) {
            throw new ParseException(ERROR_MISSING_DESCRIPTION);
        }

        Optional<String> description = Optional.of(first);
        Optional<LocalDateTime> dueDate;
        Optional<TaskStatus> status;

        try {
            dueDate = Optional.of(ParserUtil.parseAndValidateDueDate(second));
        } catch (ParseException e) {
            throw new ParseException(ERROR_INVALID_SECOND_DATE);
        }

        if (TaskStatus.isValidStatus(third)) {
            status = Optional.of(TaskStatus.fromString(third));
        } else {
            throw new ParseException(ERROR_INVALID_THIRD_STATUS);
        }

        ensureAtLeastOneField(description, dueDate, status);
        return new UpdateTaskCommand(personIndex, taskIndex, description, dueDate, status);
    }

    private void ensureAtLeastOneField(Optional<String> description,
                                       Optional<LocalDateTime> dueDate,
                                       Optional<TaskStatus> status) throws ParseException {
        if (description.isEmpty() && dueDate.isEmpty() && status.isEmpty()) {
            throw new ParseException(Messages.MESSAGE_NOT_UPDATED);
        }
    }
}
