package staffconnect.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.logic.commands.CommandTestUtil.VALID_DATE_MARCH;
import static staffconnect.logic.commands.CommandTestUtil.VALID_DESCRIPTION_MIDTERMS;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING_APRIL;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING_FINALS;
import static staffconnect.logic.commands.CommandTestUtil.assertCommandFailure;
import static staffconnect.logic.commands.CommandTestUtil.assertCommandSuccessWithPerson;
import static staffconnect.logic.commands.CommandTestUtil.showPersonAtIndex;
import static staffconnect.model.meeting.comparator.MeetingDateThenDescriptionComparator.MEETING_DATE_THEN_DESCRIPTION_COMPARATOR;
import static staffconnect.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static staffconnect.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static staffconnect.testutil.TypicalPersons.getTypicalStaffBook;

import org.junit.jupiter.api.Test;

import staffconnect.commons.core.index.Index;
import staffconnect.logic.Messages;
import staffconnect.model.Model;
import staffconnect.model.ModelManager;
import staffconnect.model.StaffBook;
import staffconnect.model.UserPrefs;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.meeting.MeetingDateTime;
import staffconnect.model.meeting.MeetingDescription;
import staffconnect.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for AddMeetingCommand.
 */
public class AddMeetingCommandTest {

    private static final Model TEST_MODEL = new ModelManager(getTypicalStaffBook(), new UserPrefs());

    private Person buildValidPerson() {
        Person pickPerson = TEST_MODEL.getSortedFilteredPersonList().get(0);
        return new Person(pickPerson.getName(), pickPerson.getPhone(), pickPerson.getEmail(),
                pickPerson.getModule(), pickPerson.getFaculty(), pickPerson.getVenue(),
                pickPerson.getTags(), pickPerson.getAvailabilities(), pickPerson.getFavourite());
    }

    @Test
    public void execute_allFieldsValid_success() {

        //Requires both filtered predicate to be the same
        TEST_MODEL.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        AddMeetingCommand addMeetingCommand = new AddMeetingCommand(INDEX_FIRST_PERSON, VALID_MEETING);

        String expectedMessage = String.format(AddMeetingCommand.MESSAGE_SUCCESS, Messages.format(VALID_MEETING));

        Model expectedModel = new ModelManager(new StaffBook(TEST_MODEL.getStaffBook()), new UserPrefs());
        expectedModel.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);

        Person validPerson = buildValidPerson();
        validPerson.addMeetings(VALID_MEETING);
        validPerson.updateSortedMeetingList(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR);
        expectedModel.setPerson(expectedModel.getSortedFilteredPersonList().get(0), validPerson);

        assertCommandSuccessWithPerson(addMeetingCommand, TEST_MODEL,
                expectedMessage, expectedModel, validPerson, 0);
    }

    @Test
    public void execute_duplicateMeeting_failure() {
        Person firstPerson = buildValidPerson();
        firstPerson.addMeetings(VALID_MEETING);

        AddMeetingCommand addMeetingCommand = new AddMeetingCommand(INDEX_FIRST_PERSON, VALID_MEETING);

        Model stubModel = new ModelManager(new StaffBook(TEST_MODEL.getStaffBook()), new UserPrefs());
        stubModel.setPerson(TEST_MODEL.getSortedFilteredPersonList().get(0), firstPerson);

        assertCommandFailure(addMeetingCommand, stubModel, AddMeetingCommand.MESSAGE_DUPLICATE_MEETING);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(TEST_MODEL.getSortedFilteredPersonList().size() + 1);

        AddMeetingCommand addMeetingCommand = new AddMeetingCommand(outOfBoundIndex, VALID_MEETING);

        assertCommandFailure(addMeetingCommand, TEST_MODEL, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(TEST_MODEL, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;

        // ensures that outOfBoundIndex is still in bounds of staff book list
        assertTrue(outOfBoundIndex.getZeroBased() < TEST_MODEL.getStaffBook().getPersonList().size());
        AddMeetingCommand addMeetingCommand = new AddMeetingCommand(outOfBoundIndex, VALID_MEETING);

        assertCommandFailure(addMeetingCommand, TEST_MODEL, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final AddMeetingCommand standardCommand = new AddMeetingCommand(INDEX_FIRST_PERSON, VALID_MEETING);
        // same values -> returns true
        final Meeting copyMeeting =
                new Meeting(new MeetingDescription(VALID_DESCRIPTION_MIDTERMS), new MeetingDateTime(VALID_DATE_MARCH));
        AddMeetingCommand commandWithSameValues = new AddMeetingCommand(INDEX_FIRST_PERSON, copyMeeting);
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(null, standardCommand);

        // different types -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // different index -> returns false
        assertNotEquals(standardCommand, new AddMeetingCommand(INDEX_SECOND_PERSON, VALID_MEETING));

        // different description -> returns false
        assertNotEquals(standardCommand, new AddMeetingCommand(INDEX_FIRST_PERSON, VALID_MEETING_FINALS));

        // different date -> returns false
        assertNotEquals(standardCommand, new AddMeetingCommand(INDEX_FIRST_PERSON, VALID_MEETING_APRIL));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        AddMeetingCommand addMeetingCommand = new AddMeetingCommand(index, VALID_MEETING);
        String expected =
                AddMeetingCommand.class.getCanonicalName() + "{index=" + index + ", toAdd=" + VALID_MEETING + "}";
        assertEquals(expected, addMeetingCommand.toString());
    }

}

