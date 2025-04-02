package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.task.Task;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label telegram;
    @FXML
    private Label position;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private FlowPane skills;
    @FXML
    private FlowPane others;
    @FXML
    private FlowPane tasks;
    @FXML
    private HBox tagsContainer;
    @FXML
    private HBox skillsContainer;
    @FXML
    private HBox othersContainer;
    @FXML
    private HBox tasksContainer;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText("Phone: " + person.getPhone().value);
        telegram.setText("Telegram: " + person.getTelegram().value);
        position.setText("Position: " + person.getPosition().value);
        address.setText("Address: " + person.getAddress().value);
        email.setText("Email: " + person.getEmail().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        person.getSkills().stream()
                .sorted(Comparator.comparing(skill -> skill.skillName))
                .forEach(skill -> skills.getChildren().add(new Label(skill.skillName)));
        person.getOthers().stream()
                .sorted(Comparator.comparing(other -> other.other))
                .forEach(other -> others.getChildren().add(new Label(other.other)));
        person.getTasks().stream()
            .forEach((Task task) -> {
                Label taskLabel = new Label(task.getDescription()
                                            + " ["
                                            + task.getStatus().name().toLowerCase().replace("_", " ")
                                            + "]");
                taskLabel.getStyleClass().add("task-label");
                tasks.getChildren().add(taskLabel);
            });
        if (person.getTags().isEmpty()) {
            tagsContainer.setManaged(false);
            tagsContainer.setVisible(false);
        }
        if (person.getSkills().isEmpty()) {
            skillsContainer.setManaged(false);
            skillsContainer.setVisible(false);
        }
        if (person.getOthers().isEmpty()) {
            othersContainer.setManaged(false);
            othersContainer.setVisible(false);
        }
        if (person.getTasks().isEmpty()) {
            tasksContainer.setManaged(false);
            tasksContainer.setVisible(false);
        }
    }
}
