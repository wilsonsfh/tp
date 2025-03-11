package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seedu.address.model.task.Task;

public class JsonAdaptedTask {

    private final String description;

    @JsonCreator
    public JsonAdaptedTask(@JsonProperty("description") String description) {
        this.description = description;
    }

    public JsonAdaptedTask(Task source) {
        this.description = source.getDescription();
    }

    public Task toModelType() {
        return new Task(description); // No validation yet
    }
}

