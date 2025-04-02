package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;
    private final List<Person> completedTasks;
    private final List<Person> inProgressTasks;
    private final List<Person> yetToStartTasks;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    private final String taskReport;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     * Initializes task lists to empty.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.taskReport = null;
        this.completedTasks = Collections.emptyList();
        this.inProgressTasks = Collections.emptyList();
        this.yetToStartTasks = Collections.emptyList();
    }

    /**
     * Constructs a {@code CommandResult} with task lists.
     */
    public CommandResult(String feedbackToUser,
                         List<Person> completedTasks,
                         List<Person> inProgressTasks,
                         List<Person> yetToStartTasks) {
        this.feedbackToUser = feedbackToUser;
        this.completedTasks = completedTasks;
        this.inProgressTasks = inProgressTasks;
        this.yetToStartTasks = yetToStartTasks;
        this.showHelp = false;
        this.exit = false;
        this.taskReport = null;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    // New constructor for task reports using a report string
    private CommandResult(String feedbackToUser, String taskReport) {
        this.feedbackToUser = feedbackToUser;
        this.showHelp = false;
        this.exit = false;
        this.taskReport = taskReport;
        this.completedTasks = Collections.emptyList();
        this.inProgressTasks = Collections.emptyList();
        this.yetToStartTasks = Collections.emptyList();
    }

    public static CommandResult withReport(String feedback, String report) {
        return new CommandResult(feedback, report);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public List<Person> getCompletedTasks() {
        return completedTasks;
    }

    public List<Person> getInProgressTasks() {
        return inProgressTasks;
    }

    public List<Person> getYetToStartTasks() {
        return yetToStartTasks;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    // New methods
    public boolean hasReport() {
        return taskReport != null;
    }

    public String getTaskReport() {
        return taskReport;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CommandResult)) {
            return false;
        }
        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .toString();
    }
}
