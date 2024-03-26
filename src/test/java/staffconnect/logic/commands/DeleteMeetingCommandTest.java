package staffconnect.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING_STUDY;
import static staffconnect.logic.commands.CommandTestUtil.assertCommandFailure;
import static staffconnect.logic.commands.CommandTestUtil.assertCommandSuccess;
import static staffconnect.logic.commands.CommandTestUtil.showMeetingAtIndex;
import static staffconnect.testutil.TypicalIndexes.INDEX_FIRST_MEETING;
import static staffconnect.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static staffconnect.testutil.TypicalIndexes.INDEX_SECOND_MEETING;
import static staffconnect.testutil.TypicalPersons.getTypicalStaffBook;

import org.junit.jupiter.api.Test;

import staffconnect.commons.core.index.Index;
import staffconnect.logic.Messages;
import staffconnect.model.Model;
import staffconnect.model.ModelManager;
import staffconnect.model.UserPrefs;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteMeetingCommand}.
 */
public class DeleteMeetingCommandTest {

    private static final Model TEST_MODEL = new ModelManager(getTypicalStaffBook(), new UserPrefs());

    private Person copyPersonWithNewMeetingManager(Person pickPerson) {
        return new Person(pickPerson.getName(), pickPerson.getPhone(), pickPerson.getEmail(),
                pickPerson.getModule(), pickPerson.getFaculty(), pickPerson.getVenue(),
                pickPerson.getTags(), pickPerson.getAvailabilities());

    }

    @Test
    public void execute_validIndexUnfilteredList_success() {

        //Set a dummy meeting first
        Person selectPerson = copyPersonWithNewMeetingManager(TEST_MODEL.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()));

        selectPerson.addMeetings(VALID_MEETING);
        TEST_MODEL.setPerson(selectPerson, selectPerson);

        DeleteMeetingCommand deleteMeetingCommand = new DeleteMeetingCommand(INDEX_FIRST_PERSON, INDEX_FIRST_MEETING);

        String expectedMessage = String.format(DeleteMeetingCommand.MESSAGE_DELETE_MEETING_SUCCESS,
                Messages.format(VALID_MEETING));

        ModelManager expectedModel = new ModelManager(getTypicalStaffBook(), new UserPrefs());

        assertCommandSuccess(deleteMeetingCommand, TEST_MODEL, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(TEST_MODEL.getFilteredPersonList().size() + 1);
        DeleteMeetingCommand deleteMeetingCommand = new DeleteMeetingCommand(outOfBoundIndex, INDEX_FIRST_MEETING);

        assertCommandFailure(deleteMeetingCommand, TEST_MODEL, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidMeetingIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(TEST_MODEL
                .getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased())
                .getFilteredMeetings().size() + 1);

        DeleteMeetingCommand deleteMeetingCommand = new DeleteMeetingCommand(INDEX_FIRST_PERSON, outOfBoundIndex);

        assertCommandFailure(deleteMeetingCommand, TEST_MODEL, Messages.MESSAGE_INVALID_MEETING_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        Person personToSelect = copyPersonWithNewMeetingManager(TEST_MODEL.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()));
        personToSelect.addMeetings(VALID_MEETING); //First valid meeting
        personToSelect.addMeetings(VALID_MEETING_STUDY); //Second valid meeting
        showMeetingAtIndex(personToSelect, INDEX_SECOND_MEETING); //Update visibility
        //update the model
        TEST_MODEL.setPerson(personToSelect, personToSelect);

        Meeting meetingToDelete = personToSelect.getFilteredMeetings().get(INDEX_FIRST_MEETING.getZeroBased());
        //deletes the second meeting in the model
        DeleteMeetingCommand deleteMeetingCommand = new DeleteMeetingCommand(INDEX_FIRST_PERSON, INDEX_FIRST_MEETING);
        String expectedMessage = String.format(DeleteMeetingCommand.MESSAGE_DELETE_MEETING_SUCCESS,
                Messages.format(meetingToDelete));

        Model expectedModel = new ModelManager(getTypicalStaffBook(), new UserPrefs());
        Person expectedPersonSelect =
                copyPersonWithNewMeetingManager(expectedModel.getFilteredPersonList()
                        .get(INDEX_FIRST_PERSON.getZeroBased()));

        expectedPersonSelect.addMeetings(VALID_MEETING); //This should be the only meeting left in the model
        showNoMeeting(expectedPersonSelect); //But it is not visible
        expectedModel.setPerson(expectedPersonSelect, expectedPersonSelect);

        assertCommandSuccess(deleteMeetingCommand, TEST_MODEL, expectedMessage, expectedModel);
    }

    /**
     * Updates {@code person}'s filtered list to show no meeting.
     */

    private void showNoMeeting(Person person) {
        person.updateFilteredMeetingList(p -> false);
        assertTrue(person.getFilteredMeetings().isEmpty());
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        Person personToSelect = copyPersonWithNewMeetingManager(TEST_MODEL.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()));
        personToSelect.addMeetings(VALID_MEETING);
        personToSelect.addMeetings(VALID_MEETING_STUDY);
        showMeetingAtIndex(personToSelect, INDEX_FIRST_MEETING);
        TEST_MODEL.setPerson(personToSelect, personToSelect);

        Index outOfBoundIndex = INDEX_SECOND_MEETING;
        // ensures that outOfBoundIndex is still in bounds of person's meeting list
        assertTrue(outOfBoundIndex.getZeroBased() < personToSelect.getMeetings().size());

        DeleteMeetingCommand deleteMeetingCommand = new DeleteMeetingCommand(INDEX_FIRST_PERSON, outOfBoundIndex);

        assertCommandFailure(deleteMeetingCommand, TEST_MODEL, Messages.MESSAGE_INVALID_MEETING_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteMeetingCommand deleteFirstCommand = new DeleteMeetingCommand(INDEX_FIRST_PERSON, INDEX_FIRST_MEETING);
        DeleteMeetingCommand deleteSecondCommand = new DeleteMeetingCommand(INDEX_FIRST_PERSON, INDEX_SECOND_MEETING);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteMeetingCommand deleteFirstCommandCopy = new DeleteMeetingCommand(INDEX_FIRST_PERSON, INDEX_FIRST_MEETING);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different meeting index -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetPersonIndex = Index.fromOneBased(1);
        Index targetMeetingIndex = Index.fromOneBased(2);
        DeleteMeetingCommand deleteCommand = new DeleteMeetingCommand(targetPersonIndex, targetMeetingIndex);
        String expected = DeleteMeetingCommand.class.getCanonicalName() + "{targetPersonIndex=" + targetPersonIndex
                + ", " + "targetMeetingIndex=" + targetMeetingIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }
}
