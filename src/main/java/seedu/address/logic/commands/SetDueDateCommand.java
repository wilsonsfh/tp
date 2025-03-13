package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Set the due date for a task.
 */
public class SetDueDateCommand extends Command {
    private final LocalDateTime dueDate;
    private final Index targetIndex;

    /**
     * Creates a SetDueDateCommand to set a due date for the specified task.
     *
     * @param dueDate
     * @param targetIndex
     */
    public SetDueDateCommand(LocalDateTime dueDate, Index targetIndex) {
        this.dueDate = dueDate;
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // TODO: implement execute
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SetDueDateCommand)) {
            return false;
        }

        SetDueDateCommand otherCommand = (SetDueDateCommand) other;
        return targetIndex.equals(otherCommand.targetIndex)
                && dueDate.equals(otherCommand.dueDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("taskIndex", targetIndex)
                .add("dueDate", dueDate)
                .toString();
    }
}
