package seedu.address.ui;

import java.util.Comparator;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.person.Session;
import seedu.address.model.person.Student;

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
    private Label address;
    @FXML
    private Label remark;
    @FXML
    private FlowPane tags;
    @FXML
    private Label role;
    @FXML
    private FlowPane sessions;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        remark.setText(person.getRemark().value);
        if (person instanceof Student student) {
            student.getTags().stream()
                    .sorted(Comparator.comparing(tag -> tag.tagName))
                    .forEach(tag -> {
                        Label tagLabel = new Label(tag.tagName);
                        String styleClass = tag.tagName.toLowerCase();
                        tagLabel.getStyleClass().add(styleClass);
                        tags.getChildren().add(tagLabel);
                    });
            student.getSessions().stream().sorted(Comparator
                            .comparingInt((Session s) -> List.of("Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun")
                                    .indexOf(s.getDay().getValue()))
                            .thenComparingInt(s -> s.getTime().getStartMinutes()))
                    .forEach(s -> {
                        Label sessionLabel = new Label(s.toString().toLowerCase());
                        sessionLabel.getStyleClass().add("session-box");
                        // Optional: add specific style based on day
                        sessionLabel.getStyleClass().add(s.getDay().getValue().toLowerCase());
                        // Add right margin (or any side)
                        FlowPane.setMargin(sessionLabel, new Insets(0, 6, 0, 0)); // top, right, bottom, left
                        sessions.getChildren().add(sessionLabel);
                    });
        }
        role.setText(person.getRole().toString());
        role.getStyleClass().add(person.getRole().toString().toLowerCase());
    }
}
