package seedu.address.storage;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;

/**
 * Jackson-friendly {@link Task} for JSON serialization and deserialization.
 */
public class JsonAdaptedTask {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Task's %s field is missing!";

    private final String description;
    private final String status;
    private LocalDateTime dueDate;

    /**
     * Constructs a {@code JsonAdaptedTask} with inputted task details.
     */
    @JsonCreator
    public JsonAdaptedTask(@JsonProperty("description") String description,
                           @JsonProperty("status") String status,
                           @JsonProperty("dueDate") LocalDateTime dueDate) {
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    /**
     * Converts a {@code Task} for Jackson use.
     */
    public JsonAdaptedTask(Task source) {
        this.description = source.getDescription();
        this.status = source.getStatus().name(); // Store as string
        this.dueDate = source.getDueDate();
    }

    /**
     * Converts Jackson-friendly adapted task into model's {@code Task} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted task.
     */
    public Task toModelType() throws IllegalValueException {
        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Description"));
        }

        if (status == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Status"));
        }

        TaskStatus modelStatus;
        try {
            modelStatus = TaskStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException("Invalid task status: "
                                            + status
                                            + ". Valid statuses: YET_TO_START, IN_PROGRESS, COMPLETED.");
        }

        return new Task(description, modelStatus, dueDate);
    }
}
