package seedu.address.model.task;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Task associated with a Person in address book.
 * Guarantees to be immutable and description is non-null.
 */
public class Task {
    private final String description;
    private LocalDateTime dueDate;
    private final TaskStatus status;

    /**
     * Creates a Task with inputted description and status.
     * The due date is initialized to null, indicating that no due date has been set.
     *
     * @param description The task's description.
     * @param status The task's status.
     */
    public Task(String description, TaskStatus status, LocalDateTime dueDate) {
        requireNonNull(description);
        requireNonNull(status);
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getDueDate() {
        return this.dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Task withStatus(TaskStatus newStatus) {
        requireNonNull(newStatus);
        return new Task(this.description, newStatus, this.dueDate);
    }

    public Task withDescription(String newDescription) {
        requireNonNull(newDescription);
        return new Task(newDescription, this.status, this.dueDate);
    }

    public Task withDueDate(LocalDateTime newDueDate) {
        // newDueDate can be null, thus requireNonNull not required
        return new Task(this.description, this.status, newDueDate);
    }

    private String formatDueDate() {
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a");
        String formattedDueDate = dueDate.format(displayFormatter);

        return formattedDueDate;
    }

    @Override
    public String toString() {
        String formattedDueDate = (dueDate == null) ? "No due date set yet." : formatDueDate();
        return "DESCRIPTION: " + description
                + " | DUE DATE: " + formattedDueDate
                + " | STATUS: " + status + " |";
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
        boolean isDueDateEqual = (dueDate == null && otherTask.dueDate == null)
            || (dueDate != null && dueDate.equals(otherTask.dueDate));
        return description.equals(otherTask.description)
            && isDueDateEqual
            && status == otherTask.status;
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

}
