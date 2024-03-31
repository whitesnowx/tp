package staffconnect.ui;

import java.util.Comparator;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.person.Person;

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
    private Text name;
    @FXML
    private Label phone;
    @FXML
    private Label venue;
    @FXML
    private Label faculty;
    @FXML
    private Label module;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private FlowPane availabilities;

    @FXML
    private ListView<Meeting> meetingListView;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private StackPane displayMeetings;

    @FXML
    private SplitPane splitDisplay;

    /**
     * Creates a {@code PersonCard} with an empty card that displays nothing.
     */
    public PersonCard() {
        super(FXML);
        this.person = null;

        name.setText("");
        phone.setText("");
        faculty.setText("");
        venue.setText("");
        module.setText("");
        email.setText("");

        meetingListView.setCellFactory(listView -> new MeetingsListViewCell());

    }

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person) {
        super(FXML);
        this.person = person;

        name.setText(person.getName().fullName);
        name.setTextAlignment(TextAlignment.JUSTIFY);
        name.setWrappingWidth(1000);
        phone.setText(person.getPhone().value);
        faculty.setText("Faculty:  " + person.getFaculty().toString());
        venue.setText("Venue:  " + person.getVenue().value);
        module.setText("Module:  " + person.getModule().value);
        email.setText("Email:  " + person.getEmail().value);

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        person.getAvailabilities().stream()
                .sorted(Comparator.comparing(availability -> availability.value))
                .forEach(availability -> availabilities.getChildren().add(new Label(availability.value)));

        ObservableList<Meeting> meetingsList = person.getMeetings().stream()
                .sorted(Comparator.comparing(meeting -> meeting.getStartDate().getDateTime()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        meetingListView.setFocusTraversable(false);
        meetingListView.setItems(meetingsList);
        meetingListView.setCellFactory(listView -> new MeetingsListViewCell());

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Meetings} using a {@code MeetingsCard}.
     */
    private static class MeetingsListViewCell extends ListCell<Meeting> {

        @Override
        protected void updateItem(Meeting meeting, boolean empty) {
            super.updateItem(meeting, empty);
            //Set the ID here to overwrite the default styling in the main personListViewCell
            setId("meetingListCell");
            if (empty || meeting == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new MeetingsCard(meeting, getIndex() + 1).getRoot());
            }
        }
    }

}
