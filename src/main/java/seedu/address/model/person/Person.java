package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.other.Other;
import seedu.address.model.skill.Skill;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Telegram telegram;
    private final Position position;
    private final String taskStatus;

    // Data fields
    private final Address address;
    private final Set<Tag> tags;
    private final Set<Skill> skills;
    private final Set<Other> others;
    private final List<Task> tasks;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Telegram telegram, Position position, Address address,
                  Set<Tag> tags, Set<Skill> skills, Set<Other> others, String taskStatus, List<Task> tasks) {
        requireAllNonNull(name, phone, email, telegram, position, address, tags, skills, others, taskStatus, tasks);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.telegram = telegram;
        this.position = position;
        this.address = address;
        this.tags = new HashSet<>(tags);
        this.skills = new HashSet<>(skills);
        this.others = new HashSet<>(others);
        this.taskStatus = taskStatus;
        this.tasks = new ArrayList<>(tasks);
    }

    // Getters

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Telegram getTelegram() {
        return telegram;
    }

    public Position getPosition() {
        return position;
    }

    public Address getAddress() {
        return address;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Set<Skill> getSkills() {
        return Collections.unmodifiableSet(skills);
    }

    public Set<Other> getOthers() {
        return Collections.unmodifiableSet(others);
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns a new Person object with the additional task added.
     */
    public Person addTask(Task newTask) {
        List<Task> updatedTasks = new ArrayList<>(tasks);
        updatedTasks.add(newTask);
        // Retain the same taskStatus when adding a new task.
        return new Person(name, phone, email, telegram, position, address, tags,
                skills, others, taskStatus, updatedTasks);
    }

    /**
     * Returns a new Person object with the specified task deleted.
     *
     * @param i The index (zero-based) of the task in the person's task list.
     * @return A new Person object with the updated task list.
     */
    public Person removeTask(int i) {
        List<Task> updatedTasks = new ArrayList<>(tasks);
        updatedTasks.remove(i);

        return new Person(name, phone, email, telegram, position, address, tags,
                skills, others, taskStatus, updatedTasks);
    }

    /**
     * Returns true if both persons have the same identity fields.
     * This defines a weaker notion of equality between two persons where
     * only the name is compared. Two persons with the same name are considered the same person,
     * even if their other details differ.
     *
     * @param otherPerson the person to compare with
     * @return true if both persons have the same name, false otherwise
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }
        return otherPerson != null && otherPerson.getName().equals(getName());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Person)) {
            return false;
        }
        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("telegram", telegram)
                .add("position", position)
                .add("address", address)
                .add("tags", tags)
                .add("skills", skills)
                .add("others", others)
                .add("tasks", tasks)
                .add("taskStatus", taskStatus)
                .toString();
    }
}
