package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid. "
            + "List to check indexes.\n";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_INCORRECT_DATE_FORMAT =
            "Incorrect date format! Please input in: yyyy-mm-dd HH:mm\n";
    public static final String MESSAGE_INVALID_TASK_DISPLAYED_INDEX = "The task index provided is invalid: %d\n"
            + "Use \"listtasks PERSON_INDEX\" to check tasks index.";
    public static final String MESSAGE_EMPTY_TASK_DESC = "Task description cannot be empty! \n";
    public static final String MESSAGE_NO_TASK_FOR_MEM = "No tasks found for %s.";
    public static final String MESSAGE_INCORRECT_TASK_STATUS = "Task status can be only either: "
                                                             + "yet to start | in progress | completed";
    public static final String MESSAGE_INCORRECT_PREFIX = "The prefix you typed might be incorrect. "
            + "Check again against the command usage.";
    public static final String MESSAGE_INVALID_TASK_FORMAT = "The task should only comprise description, date and "
            + "status.";
    public static final String MESSAGE_UPDATE_TASK_SUCCESS = "Updated task for %1$s: %2$s";
    public static final String MESSAGE_NOT_UPDATED = "At least one task field parameter must be provided: "
                                                   + "TASK_DESCRIPTION | TASK_STATUS | TASK_DATE";
    public static final String MESSAGE_DUPLICATE_TASK = "Such task is already present for this person.\n"
                                                   + "You cannot update to a task description that exists!\n";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Telegram: ")
                .append(person.getTelegram())
                .append("; Position: ")
                .append(person.getPosition())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        builder.append("; Skills: ");
        person.getSkills().forEach(builder::append);
        builder.append("; Others: ");
        person.getOthers().forEach(builder::append);
        builder.append("; Tasks: ");
        person.getTasks().forEach(task -> builder.append("[" + task + "]"));
        return builder.toString();
    }

}
