package staffconnect.ui;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
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
import staffconnect.commons.core.LogsCenter;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    private static final int ROW_HEIGHT = 60; //row height of each meeting

    private static final int LABEL_MEETING_WIDTH = 10; //the width of the meeting label
    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;
    private final Logger logger = LogsCenter.getLogger(getClass());
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

        double sizeWidth = getLongestWidth(meetingsList);

        logger.info("Results for the size width : " + sizeWidth);

        meetingListView.setFocusTraversable(false);
        meetingListView.setItems(meetingsList);
        meetingListView.setCellFactory(listView -> new MeetingsListViewCell());
        //Work around to keep the pref height there
        meetingListView.setPrefHeight((meetingsList.size() * ROW_HEIGHT) + 100);
        meetingListView.setPrefWidth(sizeWidth);
        meetingListView.setFocusTraversable(false);
        meetingListView.setMouseTransparent(true);

        setUpScrollPane(displayPane, detailsCard, false, false, new Region());

        setUpScrollPane(displayMeetings, meetingListView, true, true, displayMeetings);

    }

    private double getLongestWidth(List<Meeting> meetingList) {
        double maxWidth = 0;
        for (Meeting meet : meetingList) {
            //200 is to account for the default spacing within the items
            double currentWidth = (meet.getDescription().description.length() + meet.getStartDate().toString().length())
                    * LABEL_MEETING_WIDTH + 200;

            if (currentWidth > maxWidth) {
                maxWidth = currentWidth;
            }
        }
        return maxWidth;
    }

    private void setUpScrollPane(VBox display, Region content, boolean enableHBAR, boolean swap, Region swapRegion) {
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

        if (enableHBAR) {
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
