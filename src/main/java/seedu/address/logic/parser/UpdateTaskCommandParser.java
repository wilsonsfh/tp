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

    @Override
    public UpdateTaskCommand parse(String args) throws ParseException {
        String[] parts = args.trim().split("\\s+", 3);
        if (parts.length < 3) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdateTaskCommand.MESSAGE_USAGE));
        }

        Index personIndex = ParserUtil.parseIndex(parts[0]);
        Index taskIndex = ParserUtil.parseIndex(parts[1]);
        String[] fields = parts[2].split(",", -1);

        Optional<String> description = Optional.empty();
        Optional<LocalDateTime> dueDate = Optional.empty();
        Optional<TaskStatus> status = Optional.empty();

        if (fields.length > 3) {
            throw new ParseException("Too many fields. Use: DESCRIPTION[, DUE_DATE][, STATUS]");
        }

        String first = fields[0].trim();

        if (fields.length == 1) {
            if (TaskStatus.isValidStatus(first)) {
                status = Optional.of(TaskStatus.fromString(first));
            } else {
                try {
                    dueDate = Optional.of(ParserUtil.parseAndValidateDueDate(first));
                } catch (ParseException e) {
                    description = Optional.of(first);
                }
            }
        } else if (fields.length == 2) {
            String second = fields[1].trim();

            if (TaskStatus.isValidStatus(first)) {
                throw new ParseException("Cannot update other fields when only updating task status. Use only: STATUS");
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
                        throw new ParseException("Second parameter must be a valid date (yyyy-MM-dd HH:mm) or task status.");
                    }
                }
            }
        } else if (fields.length == 3) {
            String second = fields[1].trim();
            String third = fields[2].trim();

            if (first.isEmpty()) {
                throw new ParseException("Description cannot be empty when providing multiple fields.");
            }
            description = Optional.of(first);

            try {
                dueDate = Optional.of(ParserUtil.parseAndValidateDueDate(second));
            } catch (ParseException e) {
                throw new ParseException("Second parameter must be a valid date (yyyy-MM-dd HH:mm).");
            }

            if (TaskStatus.isValidStatus(third)) {
                status = Optional.of(TaskStatus.fromString(third));
            } else {
                throw new ParseException("Third parameter must be a valid task status: 'yet to start', 'in progress', 'completed'.");
            }
        }

        if (description.isEmpty() && dueDate.isEmpty() && status.isEmpty()) {
            throw new ParseException(Messages.MESSAGE_NOT_UPDATED);
        }

        return new UpdateTaskCommand(personIndex, taskIndex, description, dueDate, status);
    }

}
