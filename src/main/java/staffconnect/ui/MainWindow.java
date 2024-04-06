package staffconnect.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import staffconnect.commons.core.GuiSettings;
import staffconnect.commons.core.LogsCenter;
import staffconnect.logic.Logic;
import staffconnect.logic.commands.CommandResult;
import staffconnect.logic.commands.exceptions.CommandException;
import staffconnect.logic.parser.exceptions.ParseException;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    private PersonCard personOnDisplay;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private StackPane personCardPanelPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());
        // The default divider is 0.43
        personOnDisplay = logic.getFirstPersonIfExist()
                .map(person -> new PersonCard(person, 0.43)).orElse(new PersonCard());

        helpWindow = new HelpWindow();
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {

        personListPanel =
                new PersonListPanel(logic.getFilteredPersonList(), this::changePersonCard, this::getDividerPosition);
        personListPanel.setListSelectedIndex(0);
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        StatusBarFooter statusBarFooter =
                new StatusBarFooter(logic.getStaffConnectFilePath(), this::handleExit, this::handleHelp);
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        personCardPanelPlaceholder.getChildren().add(personOnDisplay.getRoot());


    }

    private void changePersonCard(PersonCard personToUpdate) {

        personOnDisplay = personToUpdate;
        personCardPanelPlaceholder.getChildren().clear();
        personCardPanelPlaceholder.getChildren().add(personOnDisplay.getRoot());

    }

    /**
     * Gets the current divider position, to be used by the UI only
     *
     * @return a double value of the divider position.
     */
    public double getDividerPosition() {
        return personOnDisplay.getCurrentDividerPosition();
    }

    /**
     * Executes the command and returns the result.
     *
     * @see Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            reloadPersonCardWithRoot();

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            if (commandResult.hasPersonAndIndex()) {

                double dividerPosition = personOnDisplay.getCurrentDividerPosition();
                int index = commandResult.getIndex();
                commandResult.getPersonToDisplay()
                        .ifPresentOrElse(
                                person -> reloadPersonCardWithPerson(new PersonCard(person, dividerPosition), index),
                                this::reloadPersonCardWithRoot);
            } else {
                reloadPersonCardWithRoot();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    /**
     * Closes the application.
     */
    @FXML
    public void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    private void reloadPersonCardWithRoot() {
        double previousDividerPosition = personOnDisplay.getCurrentDividerPosition();
        personOnDisplay = logic.getFirstPersonIfExist()
                .map(person -> new PersonCard(person, previousDividerPosition)).orElse(new PersonCard());

        personCardPanelPlaceholder.getChildren().clear();
        personCardPanelPlaceholder.getChildren().add(personOnDisplay.getRoot());

        personListPanel.setListSelectedIndex(0);

    }

    private void reloadPersonCardWithPerson(PersonCard person, int index) {

        personOnDisplay = person;
        personCardPanelPlaceholder.getChildren().clear();
        personCardPanelPlaceholder.getChildren().add(personOnDisplay.getRoot());

        personListPanel.setListSelectedIndex(index);

    }

    void show() {
        primaryStage.show();
    }
}
