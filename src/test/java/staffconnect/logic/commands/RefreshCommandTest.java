package staffconnect.logic.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import staffconnect.logic.commands.exceptions.CommandException;
import staffconnect.model.Model;
import staffconnect.model.ModelManager;
import staffconnect.model.UserPrefs;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.meeting.MeetingDateTime;
import staffconnect.model.meeting.MeetingDescription;
import staffconnect.model.person.Person;

import static staffconnect.logic.commands.CommandTestUtil.assertCommandSuccess;
import static staffconnect.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static staffconnect.testutil.TypicalPersons.getTypicalStaffBook;

public class RefreshCommandTest {
    private Model model;
    private Model expectedModel;
    private Meeting outdatedMeeting;
    private Meeting upcomingMeeting;
    private CommandResult expectedResultWithNoDeletion;
    private CommandResult expectedResultWithDeletion;
    private Person validPerson;
    private RefreshCommand command;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalStaffBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getStaffBook(), new UserPrefs());
        outdatedMeeting = new Meeting(new MeetingDescription("French Revolution"),
                new MeetingDateTime("14/07/1789 12:00"));
        upcomingMeeting = new Meeting(new MeetingDescription("Future"),
                new MeetingDateTime("01/01/2999 18:00"));
        validPerson = buildValidPerson();
        expectedResultWithNoDeletion = new CommandResult(RefreshCommand.REFRESH_NO_MODIFICATION);
        expectedResultWithDeletion = new CommandResult(String.format("%s\n%s\n%s", RefreshCommand.REFRESH_SUCCESS,
                "The following meeting(s) is(are) deleted",
                validPerson.getName() + String.format(" (Index: %d) %s\n", 1, outdatedMeeting)));
        command = new RefreshCommand();
    }

    @Test
    public void noMeeting_refresh_success() throws CommandException {
        assertCommandSuccess(command, model, expectedResultWithNoDeletion, expectedModel);
    }

    @Test
    public void noMeetingDeleted_refresh_success() throws CommandException {
        AddMeetingCommand addMeetingCommand = new AddMeetingCommand(INDEX_FIRST_PERSON, upcomingMeeting);

        addMeetingCommand.execute(model);
        addMeetingCommand.execute(expectedModel);

        assertCommandSuccess(command, model, expectedResultWithNoDeletion, expectedModel);
    }

    @Test void meetingDeleted_refresh_success() throws CommandException {

        AddMeetingCommand addMeetingCommand = new AddMeetingCommand(INDEX_FIRST_PERSON, outdatedMeeting);

        addMeetingCommand.execute(model);

        assertCommandSuccess(command, model, expectedResultWithDeletion, expectedModel);
    }

    private Person buildValidPerson() {
        Person pickPerson = model.getSortedFilteredPersonList().get(0);
        return new Person(pickPerson.getName(), pickPerson.getPhone(), pickPerson.getEmail(),
                pickPerson.getModule(), pickPerson.getFaculty(), pickPerson.getVenue(),
                pickPerson.getTags(), pickPerson.getAvailabilities(), pickPerson.getFavourite());
    }
}
