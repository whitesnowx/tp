package staffconnect.logic.commands;

import static staffconnect.logic.commands.CommandTestUtil.assertCommandSuccess;
import static staffconnect.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static staffconnect.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static staffconnect.testutil.TypicalPersons.getTypicalStaffBook;

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

public class RefreshCommandTest {
    private Model model;
    private Model expectedModel;
    private Meeting outdatedMeeting1;
    private Meeting outdatedMeeting2;
    private Meeting upcomingMeeting;
    private CommandResult expectedResultWithNoDeletion;
    private CommandResult expectedResultWithDeletion;
    private Person validPerson1;
    private Person validPerson2;
    private RefreshCommand command;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalStaffBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getStaffBook(), new UserPrefs());
        outdatedMeeting1 = new Meeting(new MeetingDescription("French Revolution"),
                new MeetingDateTime("14/07/1789 12:00"));
        outdatedMeeting2 = new Meeting(new MeetingDescription("Century End"),
                new MeetingDateTime("31/12/1999 23:59"));
        upcomingMeeting = new Meeting(new MeetingDescription("Future"),
                new MeetingDateTime("01/01/2999 18:00"));
        validPerson1 = buildValidPerson(0);
        validPerson2 = buildValidPerson(1);
        expectedResultWithNoDeletion = new CommandResult(RefreshCommand.REFRESH_NO_MODIFICATION);
        expectedResultWithDeletion = new CommandResult(String.format("%s\n%s\n%s%s", RefreshCommand.REFRESH_SUCCESS,
                "The following meeting(s) is(are) deleted",
                validPerson1.getName() + String.format(" (Index: %d) %s\n", 1, outdatedMeeting1),
                validPerson2.getName() + String.format(" (Index: %d) %s\n", 2, outdatedMeeting2)));
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

    @Test
    void meetingDeleted_refresh_success() throws CommandException {

        AddMeetingCommand addMeetingCommand1 = new AddMeetingCommand(INDEX_FIRST_PERSON, outdatedMeeting1);
        AddMeetingCommand addMeetingCommand2 = new AddMeetingCommand(INDEX_SECOND_PERSON, outdatedMeeting2);

        addMeetingCommand1.execute(model);
        addMeetingCommand2.execute(model);

        assertCommandSuccess(command, model, expectedResultWithDeletion, expectedModel);
    }

    @Test
    void someMeetingsDeleted_refresh_success() throws CommandException {
        AddMeetingCommand addMeetingCommand1 = new AddMeetingCommand(INDEX_FIRST_PERSON, outdatedMeeting1);
        AddMeetingCommand addMeetingCommand2 = new AddMeetingCommand(INDEX_SECOND_PERSON, outdatedMeeting2);
        AddMeetingCommand addMeetingCommand3 = new AddMeetingCommand(INDEX_FIRST_PERSON, upcomingMeeting);

        addMeetingCommand1.execute(model);
        addMeetingCommand2.execute(model);
        addMeetingCommand3.execute(model);
        addMeetingCommand3.execute(expectedModel);

        assertCommandSuccess(command, model, expectedResultWithDeletion, expectedModel);
    }

    private Person buildValidPerson(int i) {
        Person pickPerson = model.getSortedFilteredPersonList().get(i);
        return new Person(pickPerson.getName(), pickPerson.getPhone(), pickPerson.getEmail(),
                pickPerson.getModule(), pickPerson.getFaculty(), pickPerson.getVenue(),
                pickPerson.getTags(), pickPerson.getAvailabilities(), pickPerson.getFavourite());
    }
}
