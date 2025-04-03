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
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                UpdateTaskCommand.MESSAGE_USAGE));
        }

        Index personIndex = ParserUtil.parseIndex(parts[0]);
        Index taskIndex = ParserUtil.parseIndex(parts[1]);
        String fieldSegment = parts[2].trim();

        Optional<String> description = Optional.empty();
        Optional<LocalDateTime> dueDate = Optional.empty();
        Optional<TaskStatus> status = Optional.empty();

        // Case 1: Status-only update (e.g. updatetask 1 2 completed)
        try {
            status = Optional.of(TaskStatus.fromString(fieldSegment));
            return new UpdateTaskCommand(personIndex, taskIndex, description, dueDate, status);
        } catch (IllegalArgumentException ignored) {
            // Fall through to parse as multiple fields
        }

        // Case 2: Parse comma-separated fields
        String[] fields = fieldSegment.split(",");

        if (fields.length >= 1) {
            String descCandidate = fields[0].trim();
            if (!descCandidate.isEmpty()) {
                description = Optional.of(descCandidate);
            }
        }

        if (fields.length == 2) {
            String second = fields[1].trim();
            try {
                dueDate = Optional.of(ParserUtil.parseAndValidateDueDate(second));
            } catch (ParseException pe) {
                try {
                    status = Optional.of(TaskStatus.fromString(second));
                } catch (IllegalArgumentException e) {
                    throw new ParseException("Second parameter must be a valid date (yyyy-MM-dd HH:mm) or task status.");
                }
            }
        }

        if (fields.length == 3) {
            try {
                dueDate = Optional.of(ParserUtil.parseAndValidateDueDate(fields[1].trim()));
            } catch (ParseException e) {
                throw new ParseException("Second field must be a valid due date in yyyy-MM-dd HH:mm format.");
            }

            try {
                status = Optional.of(TaskStatus.fromString(fields[2].trim()));
            } catch (IllegalArgumentException e) {
                throw new ParseException("Third field must be a valid status: 'yet to start', 'in progress', or 'completed'.");
            }
        }

        if (!description.isPresent() && !dueDate.isPresent() && !status.isPresent()) {
            throw new ParseException(Messages.MESSAGE_NOT_UPDATED);
        }

        return new UpdateTaskCommand(personIndex, taskIndex, description, dueDate, status);
    }
}
