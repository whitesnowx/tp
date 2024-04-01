package staffconnect.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.logic.commands.CommandTestUtil.assertCommandFailure;
import static staffconnect.logic.commands.CommandTestUtil.assertCommandSuccess;
import static staffconnect.logic.commands.CommandTestUtil.showPersonAtIndex;
import static staffconnect.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static staffconnect.testutil.TypicalIndexes.INDEX_FOURTH_PERSON;
import static staffconnect.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static staffconnect.testutil.TypicalPersons.getTypicalStaffBook;

import org.junit.jupiter.api.Test;

import staffconnect.commons.core.index.Index;
import staffconnect.logic.Messages;
import staffconnect.model.Model;
import staffconnect.model.ModelManager;
import staffconnect.model.StaffBook;
import staffconnect.model.UserPrefs;
import staffconnect.model.person.Person;
import staffconnect.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code MarkCommand}.
 */
public class MarkCommandTest {

    private Model model = new ModelManager(getTypicalStaffBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToMark = model.getSortedFilteredPersonList().get(INDEX_FOURTH_PERSON.getZeroBased());
        Person markedPerson = new PersonBuilder(personToMark).withFavourite(true).build();
        MarkCommand markCommand = new MarkCommand(INDEX_FOURTH_PERSON);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_PERSON_SUCCESS,
                Messages.format(markedPerson));

        ModelManager expectedModel = new ModelManager(model.getStaffBook(), new UserPrefs());
        expectedModel.setPerson(model.getSortedFilteredPersonList().get(3), markedPerson);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getSortedFilteredPersonList().size() + 1);
        MarkCommand markCommand = new MarkCommand(outOfBoundIndex);

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FOURTH_PERSON);

        Person personToMark = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person markedPerson = new PersonBuilder(personToMark).withFavourite(true).build();
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(MarkCommand.MESSAGE_MARK_PERSON_SUCCESS,
                Messages.format(markedPerson));

        Model expectedModel = new ModelManager(new StaffBook(model.getStaffBook()), new UserPrefs());
        expectedModel.setPerson(model.getSortedFilteredPersonList().get(0), markedPerson);

        assertCommandSuccess(markCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of staff book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getStaffBook().getPersonList().size());

        MarkCommand markCommand = new MarkCommand(outOfBoundIndex);

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_markMarkedPerson_throwsCommandException() {
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON);

        assertCommandFailure(markCommand, model, MarkCommand.MESSAGE_DUPLICATE_MARK);
    }

    @Test
    public void equals() {
        MarkCommand markFirstCommand = new MarkCommand(INDEX_FIRST_PERSON);
        MarkCommand markSecondCommand = new MarkCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(markFirstCommand.equals(markFirstCommand));

        // same values -> returns true
        MarkCommand markFirstCommandCopy = new MarkCommand(INDEX_FIRST_PERSON);
        assertTrue(markFirstCommand.equals(markFirstCommandCopy));

        // different types -> returns false
        assertFalse(markFirstCommand.equals(1));

        // null -> returns false
        assertFalse(markFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(markFirstCommand.equals(markSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        MarkCommand markCommand = new MarkCommand(targetIndex);
        String expected = MarkCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, markCommand.toString());
    }
}
