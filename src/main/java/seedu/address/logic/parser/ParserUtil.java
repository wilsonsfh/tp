package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_EMPTY_TASK_DESC;
import static seedu.address.logic.Messages.MESSAGE_INCORRECT_DATE_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INCORRECT_TASK_STATUS;
import static seedu.address.logic.Messages.MESSAGE_INVALID_TASK_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DUE_DATE;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.other.Other;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Position;
import seedu.address.model.person.Telegram;
import seedu.address.model.skill.Skill;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not an integer greater zero.";
    public static final String MESSAGE_STATUS_AS_DESCRIPTION =
        "Looks like you tried to supply a task status ('%s') in the description field.\n"
            + "Use a comma to separate them:\n"
            + "e.g., task/TASK_DESCRIPTION, %s";

    public static final String MESSAGE_DATE_AS_DESCRIPTION =
        "Looks like you tried to supply a due date ('%s') in the description field.\n"
            + "Use a comma to separate them:\n"
            + "e.g., task/TASK_DESCRIPTION, %s";

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String telegram} into an {@code Telegram}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code telegram} is invalid.
     */
    public static Telegram parseTelegram(String telegram) throws ParseException {
        requireNonNull(telegram);
        String trimmedTelegram = telegram.trim();
        if (!Telegram.isValidTelegram(trimmedTelegram)) {
            throw new ParseException(Telegram.MESSAGE_CONSTRAINTS);
        }
        return new Telegram(trimmedTelegram);
    }

    /**
     * Parses a {@code String position} into an {@code Position}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Position parsePosition(String position) {
        requireNonNull(position);
        String trimmedPosition = position.trim();
        return new Position(trimmedPosition);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String Skill} into a {@code Skill}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code skill} is invalid.
     */
    public static Skill parseSkill(String skill) throws ParseException {
        requireNonNull(skill);
        String trimmedSkill = skill.trim();
        return new Skill(trimmedSkill);
    }

    /**
     * Parses {@code Collection<String> skills} into a {@code Set<Skill>}.
     */
    public static Set<Skill> parseSkills(Collection<String> skills) throws ParseException {
        requireNonNull(skills);
        final Set<Skill> skillSet = new HashSet<>();
        for (String skillName : skills) {
            skillSet.add(parseSkill(skillName));
        }
        return skillSet;
    }

    /**
     * Parses a {@code String other} into a {@code Other}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code Other} is invalid.
     */
    public static Other parseOther(String other) throws ParseException {
        requireNonNull(other);
        String trimmedOther = other.trim();
        return new Other(trimmedOther);
    }

    /**
     * Parses {@code Collection<String> others} into a {@code Set<Other>}.
     */
    public static Set<Other> parseOthers(Collection<String> others) throws ParseException {
        requireNonNull(others);
        final Set<Other> otherSet = new HashSet<>();
        for (String other : others) {
            otherSet.add(parseOther(other));
        }
        return otherSet;
    }

    /**
     * Parses a {@code String task} into a {@code Task}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code Task} is invalid.
     */
    public static Task parseTask(String task) throws ParseException {
        requireNonNull(task);

        if (task.trim().isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_TASK_DESC); //Edge case to ensure task/ is not empty
        }

        String[] taskDetails = task.split(",");

        return switch (taskDetails.length) {
        case 3 -> parseThreeVariableTask(taskDetails[0].trim(), taskDetails[1].trim(), taskDetails[2].trim());
        case 2 -> parseTwoVariableTask(taskDetails[0].trim(), taskDetails[1].trim());
        case 1 -> parseOneVariableTask(taskDetails[0].trim());
        default -> throw new ParseException(MESSAGE_INVALID_TASK_FORMAT);
        };
    }

    private static Task parseOneVariableTask(String taskDesc) throws ParseException {
        validateTaskDescription(taskDesc);
        String normalizedTaskDesc = taskDesc.trim().toLowerCase();
        if (normalizedTaskDesc.equals("completed")
            || normalizedTaskDesc.equals("in progress")
            || normalizedTaskDesc.equals("yet to start")) {
            throw new ParseException(
                String.format(MESSAGE_STATUS_AS_DESCRIPTION, taskDesc.trim(), taskDesc.trim()));
        }

        try {
            LocalDateTime.parse(taskDesc.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            throw new ParseException(
                String.format(MESSAGE_DATE_AS_DESCRIPTION, taskDesc.trim(), taskDesc.trim()));
        } catch (DateTimeParseException e) {
            // Not a valid due date, continue
        }

        return new Task(taskDesc, TaskStatus.YET_TO_START, null);
    }

    private static Task parseTwoVariableTask(String taskDesc, String secondParameter) throws ParseException {
        validateTaskDescription(taskDesc);
        try {
            LocalDateTime.parse(taskDesc.trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            throw new ParseException(String.format(MESSAGE_DATE_AS_DESCRIPTION, taskDesc.trim(), taskDesc.trim()));
        } catch (DateTimeParseException ignored) {
            // not a date, continue
        }

        try {
            TaskStatus taskStatus = TaskStatus.fromString(secondParameter);
            return new Task(taskDesc, taskStatus, null);
        } catch (IllegalArgumentException e) {
            try {
                LocalDateTime dueDate = parseAndValidateDueDate(secondParameter);
                return new Task(taskDesc, TaskStatus.YET_TO_START, dueDate);
            } catch (DateTimeParseException e1) {
                throw new ParseException(MESSAGE_INCORRECT_DATE_FORMAT);
            }
        }
    }

    private static Task parseThreeVariableTask(String taskDesc, String dueDateString, String taskStatusString)
            throws ParseException {
        StringBuilder errorMessage = new StringBuilder();
        validateTaskDescription(taskDesc);

        validateDueDateAndStatus(dueDateString, taskStatusString, errorMessage);

        if (!errorMessage.isEmpty()) {
            throw new ParseException(errorMessage.toString().trim());
        }

        LocalDateTime dueDate = parseDueDateSilently(dueDateString);
        TaskStatus taskStatus = parseTaskStatusSilently(taskStatusString);

        return new Task(taskDesc, taskStatus, dueDate);
    }

    private static void validateDueDateAndStatus(String duedateString, String taskStatusString,
                                                 StringBuilder errorMessage) {
        try {
            parseAndValidateDueDate(duedateString);
        } catch (ParseException e) {
            errorMessage.append(e.getMessage());
        }

        try {
            TaskStatus.fromString(taskStatusString);
        } catch (IllegalArgumentException e) {
            if (!errorMessage.isEmpty()) {
                errorMessage.append("\n");
            }
            errorMessage.append(MESSAGE_INCORRECT_TASK_STATUS);
        }

    }

    private static LocalDateTime parseDueDateSilently(String dueDateString) {
        try {
            return parseAndValidateDueDate(dueDateString);
        } catch (ParseException e) {
            return null; // Validated earlier
        }
    }

    private static TaskStatus parseTaskStatusSilently(String taskStatusString) {
        try {
            return TaskStatus.fromString(taskStatusString);
        } catch (IllegalArgumentException e) {
            return null; // Validated earlier
        }
    }

    /**
     * Parses {@code Collection<String> tasks} into a {@code Set<Task>}.
     */
    public static Set<Task> parseTasks(Collection<String> tasks) throws ParseException {
        requireNonNull(tasks);
        final Set<Task> taskSet = new HashSet<>();
        for (String task : tasks) {
            taskSet.add(parseTask(task));
        }
        return taskSet;
    }

    /**
     * Parses the due date provided by the user from the given {@code ArgumentMultimap},
     * expecting the value associated with {@code PREFIX_DUE_DATE} to be in String format.
     *
     * @param argMultimap The map containing argument prefixes and their corresponding values.
     * @return The parsed {@code LocalDateTime} due date.
     * @throws ParseException If the due date is not in the expected format.
     */
    public static LocalDateTime parseDueDate(ArgumentMultimap argMultimap) throws ParseException {
        LocalDateTime dueDate;

        // handles parsing date
        try {
            String dateString = argMultimap.getValue(PREFIX_DUE_DATE).get();
            dueDate = LocalDateTime.parse(dateString, INPUT_FORMATTER);
            if (dueDate.isBefore(LocalDateTime.now())) {
                throw new ParseException("Due date is in the past!");
            }

        } catch (DateTimeParseException e) {
            throw new ParseException(MESSAGE_INCORRECT_DATE_FORMAT);
        }

        return dueDate;
    }

    /**
     * Handles edge case to ensure instance where task/ or taskdesc in task/taskdesc, ... is empty.
     * @param taskDesc
     * @throws ParseException
     */
    private static void validateTaskDescription(String taskDesc) throws ParseException {
        if (taskDesc.isEmpty()) {
            throw new ParseException(MESSAGE_EMPTY_TASK_DESC);
        }
    }

    /**
     * Parses and validates a due date string into a LocalDateTime.
     * Throws a ParseException if the format is invalid or the date is in the past.
     *
     * @param dueDateString The due date string.
     * @return A validated LocalDateTime object.
     * @throws ParseException if format is wrong or date is in the past.
     */
    public static LocalDateTime parseAndValidateDueDate(String dueDateString) throws ParseException {
        LocalDateTime dueDate;
        try {
            dueDate = LocalDateTime.parse(dueDateString.trim(), INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ParseException(MESSAGE_INCORRECT_DATE_FORMAT);
        }

        if (dueDate.isBefore(LocalDateTime.now())) {
            throw new ParseException("Due date is in the past!");
        }

        return dueDate;
    }
}
