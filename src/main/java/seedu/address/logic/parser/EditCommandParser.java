package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OTHER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SKILL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.other.Other;
import seedu.address.model.skill.Skill;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TELEGRAM,
                        PREFIX_POSITION, PREFIX_ADDRESS, PREFIX_TAG, PREFIX_SKILL, PREFIX_OTHER, PREFIX_TASK);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_TELEGRAM, PREFIX_POSITION);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        if (argMultimap.getValue(PREFIX_TELEGRAM).isPresent()) {
            editPersonDescriptor.setTelegram(ParserUtil.parseTelegram(argMultimap.getValue(PREFIX_TELEGRAM).get()));
        }
        if (argMultimap.getValue(PREFIX_POSITION).isPresent()) {
            editPersonDescriptor.setPosition(ParserUtil.parsePosition(argMultimap.getValue(PREFIX_POSITION).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);
        parseSkillsForEdit(argMultimap.getAllValues(PREFIX_SKILL)).ifPresent(editPersonDescriptor::setSkills);
        parseOthersForEdit(argMultimap.getAllValues(PREFIX_OTHER)).ifPresent(editPersonDescriptor::setOthers);
        parseTasksForEdit(argMultimap.getAllValues(PREFIX_TASK)).ifPresent(editPersonDescriptor::setTasks);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Skill>} if {@code skills} is non-empty.
     * If {@code skills} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Skill>} containing zero skills.
     */
    private Optional<Set<Skill>> parseSkillsForEdit(Collection<String> skills) throws ParseException {
        assert skills != null;

        if (skills.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> skillSet = skills.size() == 1 && skills.contains("") ? Collections.emptySet() : skills;
        return Optional.of(ParserUtil.parseSkills(skillSet));
    }

    /**
     * Parses {@code Collection<String> others} into a {@code Set<Other>} if {@code others} is non-empty.
     * If {@code others} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Other>} containing zero others.
     */
    private Optional<Set<Other>> parseOthersForEdit(Collection<String> others) throws ParseException {
        assert others != null;

        if (others.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> otherSet = others.size() == 1 && others.contains("") ? Collections.emptySet() : others;
        return Optional.of(ParserUtil.parseOthers(otherSet));
    }

    /**
     * Parses {@code Collection<String> tasks} into a {@code Set<Task>} if {@code tasks} is non-empty.
     * If {@code tasks} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Task>} containing zero others.
     */
    private Optional<List<Task>> parseTasksForEdit(Collection<String> tasks) throws ParseException {
        assert tasks != null;

        if (tasks.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> taskSet = tasks.size() == 1 && tasks.contains("") ? Collections.emptyList() : tasks;
        return Optional.of(ParserUtil.parseTasks(taskSet).stream().toList());
    }
}
