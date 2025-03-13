package seedu.address.model.task;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Task associated with a Person in address book.
 * Guarantees to be immutable and description is non-null.
 */
public class Task {
    private final String description;
    private final TaskStatus status;

    /**
     * Creates a Task with inputted description and status.
     *
     * @param description The task's description.
     * @param status The task's status.
     */
    public Task(String description, TaskStatus status) {
        requireNonNull(description);
        requireNonNull(status);
        this.description = description;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Task withStatus(TaskStatus newStatus) {
        return new Task(this.description, newStatus);
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;
        return description.equals(otherTask.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
