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
 * Contains integration tests (interaction with the Model) and unit tests for {@code UnfavCommand}.
 */
public class UnfavCommandTest {

    private Model model = new ModelManager(getTypicalStaffBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToUnfav = model.getSortedFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Person unfavPerson = new PersonBuilder(personToUnfav).withFavourite(false).build();
        UnfavCommand unfavCommand = new UnfavCommand(INDEX_SECOND_PERSON);

        String expectedMessage = String.format(UnfavCommand.MESSAGE_REMOVE_PERSON_FAVOURITE_SUCCESS,
                Messages.format(unfavPerson));

        ModelManager expectedModel = new ModelManager(model.getStaffBook(), new UserPrefs());
        expectedModel.setPerson(model.getSortedFilteredPersonList().get(1), unfavPerson);

        assertCommandSuccess(unfavCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getSortedFilteredPersonList().size() + 1);
        UnfavCommand unfavCommand = new UnfavCommand(outOfBoundIndex);

        assertCommandFailure(unfavCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Person personToUnfav = model.getSortedFilteredPersonList().get(0);
        Person unfavPerson = new PersonBuilder(personToUnfav).withFavourite(false).build();
        UnfavCommand unfavCommand = new UnfavCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(UnfavCommand.MESSAGE_REMOVE_PERSON_FAVOURITE_SUCCESS,
                Messages.format(unfavPerson));

        Model expectedModel = new ModelManager(new StaffBook(model.getStaffBook()), new UserPrefs());
        expectedModel.setPerson(model.getSortedFilteredPersonList().get(0), unfavPerson);

        assertCommandSuccess(unfavCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of staff book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getStaffBook().getPersonList().size());

        UnfavCommand unfavCommand = new UnfavCommand(outOfBoundIndex);

        assertCommandFailure(unfavCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_unfavNotFavouritePerson_throwsCommandException() {
        UnfavCommand unfavCommand = new UnfavCommand(INDEX_FOURTH_PERSON);

        assertCommandFailure(unfavCommand, model, UnfavCommand.MESSAGE_DUPLICATE_UNFAV);
    }

    @Test
    public void equals() {
        UnfavCommand unfavFirstCommand = new UnfavCommand(INDEX_FIRST_PERSON);
        UnfavCommand unfavSecondCommand = new UnfavCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(unfavFirstCommand.equals(unfavFirstCommand));

        // same values -> returns true
        UnfavCommand unfavFirstCommandCopy = new UnfavCommand(INDEX_FIRST_PERSON);
        assertTrue(unfavFirstCommand.equals(unfavFirstCommandCopy));

        // different types -> returns false
        assertFalse(unfavFirstCommand.equals(1));

        // null -> returns false
        assertFalse(unfavFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(unfavFirstCommand.equals(unfavSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UnfavCommand unfavCommand = new UnfavCommand(targetIndex);
        String expected = UnfavCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, unfavCommand.toString());
    }
}
