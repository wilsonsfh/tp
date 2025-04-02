package seedu.address.ui;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;

/**
 * A UI component that displays a summary of tasks grouped by status.
 */
public class ReportPanel extends UiPart<Region> {
    private static final String FXML = "ReportPanel.fxml";

    @FXML
    private Label completedLabel;
    @FXML
    private VBox completedContainer;

    @FXML
    private Label inProgressLabel;
    @FXML
    private VBox inProgressContainer;

    @FXML
    private Label yetToStartLabel;
    @FXML
    private VBox yetToStartContainer;

    /**
     * Constructs a {@code ReportPanel} with the given task lists.
     *
     * @param completedTasks the list of persons with completed tasks
     * @param inProgressTasks the list of persons with tasks in progress
     * @param yetToStartTasks the list of persons with tasks yet to start
     */
    public ReportPanel(List<Person> completedTasks, List<Person> inProgressTasks, List<Person> yetToStartTasks) {
        super(FXML);
        updateReport(completedTasks, inProgressTasks, yetToStartTasks);
    }

    /**
     * Updates the report display.
     *
     * @param completedTasks list of persons with completed tasks
     * @param inProgressTasks list of persons with in progress tasks
     * @param yetToStartTasks list of persons with yet to start tasks
     */
    public void updateReport(List<Person> completedTasks, List<Person> inProgressTasks, List<Person> yetToStartTasks) {
        completedContainer.getChildren().clear();
        inProgressContainer.getChildren().clear();
        yetToStartContainer.getChildren().clear();

        completedLabel.setText("Completed Tasks (" + completedTasks.size() + "):");
        for (Person person : completedTasks) {
            completedContainer.getChildren().add(new Label(person.toString()));
        }

        inProgressLabel.setText("In Progress (" + inProgressTasks.size() + "):");
        for (Person person : inProgressTasks) {
            inProgressContainer.getChildren().add(new Label(person.toString()));
        }

        yetToStartLabel.setText("Yet to Start (" + yetToStartTasks.size() + "):");
        for (Person person : yetToStartTasks) {
            yetToStartContainer.getChildren().add(new Label(person.toString()));
        }
    }
}
