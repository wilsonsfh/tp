package seedu.address.testutil;

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

import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.other.Other;
import seedu.address.model.person.Person;
import seedu.address.model.skill.Skill;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        sb.append(PREFIX_PHONE + person.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + person.getEmail().value + " ");
        sb.append(PREFIX_TELEGRAM + person.getTelegram().value + " ");
        sb.append(PREFIX_POSITION + person.getPosition().value + " ");
        sb.append(PREFIX_ADDRESS + person.getAddress().value + " ");
        person.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        person.getSkills().stream().forEach(
                s -> sb.append(PREFIX_SKILL + s.skillName + " ")
        );
        person.getOthers().stream().forEach(
                s -> sb.append(PREFIX_OTHER + s.other + " ")
        );
        person.getTasks().stream().forEach(
                s -> sb.append(String.format("%s %s, %s, %s",
                        PREFIX_TASK,
                        s.getDescription(),
                        s.getDueDate().toString(),
                        s.getStatus().toString()))
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getTelegram().ifPresent(telegram -> sb.append(PREFIX_TELEGRAM).append(telegram.value).append(" "));
        descriptor.getPosition().ifPresent(position -> sb.append(PREFIX_POSITION).append(position.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG).append(" ");
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        if (descriptor.getSkills().isPresent()) {
            Set<Skill> skills = descriptor.getSkills().get();
            if (skills.isEmpty()) {
                sb.append(PREFIX_SKILL).append(" ");
            } else {
                skills.forEach(s -> sb.append(PREFIX_SKILL).append(s.skillName).append(" "));
            }
        }
        if (descriptor.getOthers().isPresent()) {
            Set<Other> others = descriptor.getOthers().get();
            if (others.isEmpty()) {
                sb.append(PREFIX_OTHER).append(" ");
            } else {
                others.forEach(s -> sb.append(PREFIX_OTHER).append(s.other).append(" "));
            }
        }
        if (descriptor.getTasks().isPresent()) {
            List<Task> tasks = descriptor.getTasks().get();
            if (tasks.isEmpty()) {
                sb.append(PREFIX_TASK).append(" "); // explicit clearing
            } else {
                for (Task task : tasks) {
                    sb.append(PREFIX_TASK).append(task.toString()).append(" ");
                }
            }
        }
        return sb.toString();
    }
}
