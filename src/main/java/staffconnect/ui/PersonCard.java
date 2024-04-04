package staffconnect.ui;

import java.util.Comparator;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.meeting.MeetingDateTime;
import staffconnect.model.meeting.MeetingDescription;
import staffconnect.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    //These values are used for computation of the list view height and width for different system's resolution.
    //To ensure that list view is always  displayed properly.
    private static double pixelHeight = 0;
    private static double pixelWidth = 0;

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
    private VBox displayMeetings;

    @FXML
    private SplitPane splitDisplay;

    @FXML
    private VBox displayPane;

    @FXML
    private VBox detailsCard;

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
    public PersonCard(Person person, double previousDivider) {
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

        // Set the previous divider position from the old index
        splitDisplay.setDividerPosition(0, previousDivider);

        computePixelHeight(); //Set up the pixel height variables

        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));

        person.getAvailabilities().stream()
                .sorted(Comparator.comparing(availability -> availability.value))
                .forEach(availability -> availabilities.getChildren().add(new Label(availability.value)));

        ObservableList<Meeting> meetingsList = person.getFilteredMeetings();


        setUpMeetingListView(meetingsList);

        setUpScrollPane(displayPane, detailsCard, false, false, new Region());

        setUpScrollPane(displayMeetings, meetingListView, true, true, displayMeetings);

    }

    private void computePixelHeight() {
        //Dummy meeting used for computation of font size
        Meeting dummy = new Meeting(new MeetingDescription("test"), new MeetingDateTime("12/02/2023 15:00"));
        MeetingsCard test = new MeetingsCard(dummy, 0);
        pixelHeight = test.getHeight();
        pixelWidth = test.getWidth();

    }

    private void setUpMeetingListView(ObservableList<Meeting> meetingsList) {

        meetingListView.setFocusTraversable(false);
        meetingListView.setItems(meetingsList);
        meetingListView.setCellFactory(listView -> new MeetingsListViewCell());
        //Work around to set the correct height and width of the nested list view.
        meetingListView.setPrefHeight(meetingsList.size() * (pixelHeight + (2.8 * pixelHeight)) + pixelHeight);
        meetingListView.setPrefWidth(getLongestWidth(meetingsList));
        meetingListView.setFocusTraversable(false);
        meetingListView.setMouseTransparent(true);
    }

    private void setUpScrollPane(VBox display, Region content, boolean enableHbar, boolean swap, Region swapRegion) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        //Custom vertical scroll bar on the left
        // Inspired from:
        // https://stackoverflow.com/questions/35134155/move-the-vertical-scroll-bar-of-a-scroll-panel-to-the-left-side
        ScrollBar vScrollBar = new ScrollBar();
        vScrollBar.setPrefWidth(1);
        vScrollBar.setOrientation(Orientation.VERTICAL);
        vScrollBar.minProperty().bind(scrollPane.vminProperty());
        vScrollBar.maxProperty().bind(scrollPane.vmaxProperty());
        if (swap) {
            vScrollBar.visibleAmountProperty().bind(scrollPane.heightProperty().divide(swapRegion.heightProperty()));
        } else {
            vScrollBar.visibleAmountProperty().bind(scrollPane.heightProperty().divide(content.heightProperty()));
        }
        scrollPane.vvalueProperty().bindBidirectional(vScrollBar.valueProperty());

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        HBox hBox = new HBox();
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        hBox.getChildren().addAll(vScrollBar, scrollPane);

        VBox.setVgrow(hBox, Priority.ALWAYS);

        if (enableHbar) {
            ScrollBar hScrollBar = new ScrollBar();
            hScrollBar.setOrientation(Orientation.HORIZONTAL);
            hScrollBar.minProperty().bind(scrollPane.hminProperty());
            hScrollBar.maxProperty().bind(scrollPane.hmaxProperty());
            if (swap) {
                hScrollBar.visibleAmountProperty().bind(scrollPane.widthProperty().divide(swapRegion.widthProperty()));
            } else {
                hScrollBar.visibleAmountProperty().bind(scrollPane.widthProperty().divide(content.widthProperty()));
            }
            scrollPane.hvalueProperty().bindBidirectional(hScrollBar.valueProperty());

            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            display.getChildren().addAll(hScrollBar, hBox);
        } else {
            display.getChildren().add(hBox);
        }
    }

    private double getLongestWidth(List<Meeting> meetingList) {
        double maxWidth = 0;
        Meeting longestMeeting = null;
        for (Meeting meet : meetingList) {

            double currentWidth = meet.getDescription().description.length() + meet.getStartDate().toString().length();
            if (currentWidth > maxWidth) {
                maxWidth = currentWidth;
                longestMeeting = meet;
            }

        }

        if (longestMeeting != null) {
            return (maxWidth * pixelWidth) + (pixelWidth * 15) + 3 * pixelWidth;
        } else {
            return 0; // empty meeting return zero.
        }
    }

    /**
     * Gets the current divider position for usage in the UI.
     * @return a double value of the current divider position.
     */
    public double getCurrentDividerPosition() {
        double[] positions = splitDisplay.getDividerPositions();
        return positions[0];
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
