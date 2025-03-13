package seedu.address.model.task;

import java.time.LocalDateTime;

/**
 * Public class that represents a task.
 */
public class Task {
    private final String description;
    private LocalDateTime dueDate;

    /**
     * Constructs a new Task with the specified description.
     * The due date is initialized to null, indicating that no due date has been set.
     *
     * @param description
     */
    public Task(String description) {
        this.description = description;
        this.dueDate = null;
    }

    public String getDescription() {
        return description;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
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
