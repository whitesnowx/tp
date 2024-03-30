package staffconnect.ui;

import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart<Region> {

    private static final String FXML = "StatusBarFooter.fxml";


    private ExitExecutor exitExecutor;

    private HelpExecutor helpExecutor;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private Label saveLocationStatus;

    /**
     * Creates a {@code StatusBarFooter} with the given {@code Path}.
     */
    public StatusBarFooter(Path saveLocation, ExitExecutor exitExecutor, HelpExecutor helpExecutor) {

        super(FXML);

        this.exitExecutor = exitExecutor;
        this.helpExecutor = helpExecutor;

        saveLocationStatus.setText(Paths.get(".").resolve(saveLocation).toString());

        setAccelerators();
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     *
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    @FXML
    private void handleExit() {
        exitExecutor.handleExit();
    }

    @FXML
    private void handleHelp() {
        helpExecutor.handleHelp();
    }

    /**
     * Represents a function that closes the application.
     */
    @FunctionalInterface
    public interface ExitExecutor {

        /**
         * Exits the application.
         */
        void handleExit();
    }

    /**
     * Represents a function that shows the help window.
     */
    @FunctionalInterface
    public interface HelpExecutor {

        /**
         * Shows the help window.
         */
        void handleHelp();
    }

}
