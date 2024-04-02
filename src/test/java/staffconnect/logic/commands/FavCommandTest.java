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
 * Contains integration tests (interaction with the Model) and unit tests for {@code FavCommand}.
 */
public class FavCommandTest {

    private Model model = new ModelManager(getTypicalStaffBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToFav = model.getSortedFilteredPersonList().get(INDEX_FOURTH_PERSON.getZeroBased());
        Person favPerson = new PersonBuilder(personToFav).withFavourite(true).build();
        FavCommand favCommand = new FavCommand(INDEX_FOURTH_PERSON);

        String expectedMessage = String.format(FavCommand.MESSAGE_SET_PERSON_FAVOURITE_SUCCESS,
                Messages.format(favPerson));

        ModelManager expectedModel = new ModelManager(model.getStaffBook(), new UserPrefs());
        expectedModel.setPerson(model.getSortedFilteredPersonList().get(3), favPerson);

        assertCommandSuccess(favCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getSortedFilteredPersonList().size() + 1);
        FavCommand favCommand = new FavCommand(outOfBoundIndex);

        assertCommandFailure(favCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FOURTH_PERSON);

        Person personToFav = model.getSortedFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person favPerson = new PersonBuilder(personToFav).withFavourite(true).build();
        FavCommand favCommand = new FavCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(FavCommand.MESSAGE_SET_PERSON_FAVOURITE_SUCCESS,
                Messages.format(favPerson));

        Model expectedModel = new ModelManager(new StaffBook(model.getStaffBook()), new UserPrefs());
        expectedModel.setPerson(model.getSortedFilteredPersonList().get(0), favPerson);

        assertCommandSuccess(favCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of staff book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getStaffBook().getPersonList().size());

        FavCommand favCommand = new FavCommand(outOfBoundIndex);

        assertCommandFailure(favCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_favFavouritePerson_throwsCommandException() {
        FavCommand favCommand = new FavCommand(INDEX_FIRST_PERSON);

        assertCommandFailure(favCommand, model, FavCommand.MESSAGE_DUPLICATE_FAV);
    }

    @Test
    public void equals() {
        FavCommand favFirstCommand = new FavCommand(INDEX_FIRST_PERSON);
        FavCommand favSecondCommand = new FavCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(favFirstCommand.equals(favFirstCommand));

        // same values -> returns true
        FavCommand favFirstCommandCopy = new FavCommand(INDEX_FIRST_PERSON);
        assertTrue(favFirstCommand.equals(favFirstCommandCopy));

        // different types -> returns false
        assertFalse(favFirstCommand.equals(1));

        // null -> returns false
        assertFalse(favFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(favFirstCommand.equals(favSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        FavCommand favCommand = new FavCommand(targetIndex);
        String expected = FavCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, favCommand.toString());
    }
}
