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
 * Contains integration tests (interaction with the Model) and unit tests for {@code UnmarkCommand}.
 */
public class UnmarkCommandTest {

    private Model model = new ModelManager(getTypicalStaffBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToUnmark = model.getSortedFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person unmarkedPerson = new PersonBuilder(personToUnmark).withFavourite(false).build();
        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_SECOND_PERSON);

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_PERSON_SUCCESS,
                Messages.format(unmarkedPerson));

        ModelManager expectedModel = new ModelManager(model.getStaffBook(), new UserPrefs());
        expectedModel.setPerson(model.getSortedFilteredPersonList().get(1), unmarkedPerson);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getSortedFilteredPersonList().size() + 1);
        UnmarkCommand unmarkCommand = new UnmarkCommand(outOfBoundIndex);

        assertCommandFailure(unmarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Person personToUnmark = model.getSortedFilteredPersonList().get(0);
        Person unmarkedPerson = new PersonBuilder(personToUnmark).withFavourite(false).build();
        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(UnmarkCommand.MESSAGE_UNMARK_PERSON_SUCCESS,
                Messages.format(unmarkedPerson));

        Model expectedModel = new ModelManager(new StaffBook(model.getStaffBook()), new UserPrefs());
        expectedModel.setPerson(model.getSortedFilteredPersonList().get(0), unmarkedPerson);

        assertCommandSuccess(unmarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of staff book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getStaffBook().getPersonList().size());

        UnmarkCommand unmarkCommand = new UnmarkCommand(outOfBoundIndex);

        assertCommandFailure(unmarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_unmarkUnmarkedPerson_throwsCommandException() {
        UnmarkCommand unmarkCommand = new UnmarkCommand(INDEX_FOURTH_PERSON);

        assertCommandFailure(unmarkCommand, model, UnmarkCommand.MESSAGE_DUPLICATE_UNMARK);
    }

    @Test
    public void equals() {
        UnmarkCommand unmarkFirstCommand = new UnmarkCommand(INDEX_FIRST_PERSON);
        UnmarkCommand unmarkSecondCommand = new UnmarkCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(unmarkFirstCommand.equals(unmarkFirstCommand));

        // same values -> returns true
        UnmarkCommand unmarkFirstCommandCopy = new UnmarkCommand(INDEX_FIRST_PERSON);
        assertTrue(unmarkFirstCommand.equals(unmarkFirstCommandCopy));

        // different types -> returns false
        assertFalse(unmarkFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unmarkFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(unmarkFirstCommand.equals(unmarkSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UnmarkCommand unmarkCommand = new UnmarkCommand(targetIndex);
        String expected = UnmarkCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, unmarkCommand.toString());
    }
}
