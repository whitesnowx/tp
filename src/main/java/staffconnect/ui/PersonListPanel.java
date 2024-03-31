package staffconnect.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import staffconnect.commons.core.LogsCenter;
import staffconnect.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList, PersonDisplay personDisplay) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new NameListViewCell());
        personListView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                personDisplay.changePersonCard(new PersonCard(personListView.getSelectionModel().getSelectedItem()));
            }
        });
        personListView.setOnMouseClicked(event -> {
            personDisplay.changePersonCard(new PersonCard(personListView.getSelectionModel().getSelectedItem()));
        });
    }

    /**
     * Represents a function that can change PersonCard.
     */
    @FunctionalInterface
    public interface PersonDisplay {

        /**
         * Executes the changes to the person
         */
        void changePersonCard(PersonCard toChange);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code NameCard}.
     */
    class NameListViewCell extends ListCell<Person> {
        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new NameCard(person, getIndex() + 1).getRoot());
            }
        }
    }


}
