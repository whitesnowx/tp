package staffconnect.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import staffconnect.model.person.Person;

/**
 * A UI component that displays the name only of a {@code Person}.
 */
public class NameCard extends UiPart<Region> {

    private static final String FXML = "NameCard.fxml";
    @javafx.fxml.FXML
    private Label id;
    @FXML
    private Label name;

    /**
     * Creates a {@code NameCard} with the given {@code Person} and index to display.
     */
    public NameCard(Person person, int index) {
        super(FXML);
        id.setText(index + ".  ");
        name.setText(person.getName().fullName);
    }

}
