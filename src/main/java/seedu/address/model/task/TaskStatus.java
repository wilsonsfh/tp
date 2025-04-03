package seedu.address.model.task;

/**
 * Representation of status for a particular task.
 */
public enum TaskStatus {
    YET_TO_START,
    IN_PROGRESS,
    COMPLETED;

    /**
     * Converts a {@code String} representation of a task status into a {@code TaskStatus} enum constant.
     * <p>
     * This method is case-insensitive and accepts the following string values:
     * <ul>
     *     <li>"yet to start" – maps to {@code TaskStatus.YET_TO_START}</li>
     *     <li>"in progress" – maps to {@code TaskStatus.IN_PROGRESS}</li>
     *     <li>"completed" – maps to {@code TaskStatus.COMPLETED}</li>
     * </ul>
     *
     * @param str the string representation of the task status
     * @return the corresponding {@code TaskStatus} enum constant
     * @throws IllegalArgumentException if the input string does not match any known task status
     */
    public static TaskStatus fromString(String str) {
        String normalised = str.trim().toLowerCase();
        switch (normalised) {
        case "yet to start":
            return TaskStatus.YET_TO_START;
        case "in progress":
            return TaskStatus.IN_PROGRESS;
        case "completed":
            return TaskStatus.COMPLETED;
        default:
            throw new IllegalArgumentException("Invalid task status: " + str);
        }
    }
}
