package staffconnect.logic.commands;

import static java.util.Objects.requireNonNull;
import static staffconnect.model.meeting.comparator.MeetingDateThenDescriptionComparator.MEETING_DATE_THEN_DESCRIPTION_COMPARATOR;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import staffconnect.logic.commands.exceptions.CommandException;
import staffconnect.model.Model;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.person.Person;
import staffconnect.model.person.PersonUtil;

/**
 * Deletes all meetings that are outdated.
 * An outdated meeting refers to a meeting that starts before the user types in the command.
 */
public class RefreshCommand extends Command {

    public static final String COMMAND_WORD = "refresh";

    public static final String REFRESH_SUCCESS = "Upcoming meetings successfully refreshed. "
            + "All outdated meetings are deleted.";

    public static final String REFRESH_NO_MODIFICATION = "Upcoming meetings successfully refreshed. "
            + "No meetings are outdated.";

    /**
     * Deletes all outdated meetings.
     *
     * @param model {@code Model} which the command should operate on.
     * @return The detailed information of all deleted meetings
     * @throws CommandException if the execution is unsuccessful.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        LocalDateTime currentTime = LocalDateTime.now();
        List<Person> lastShownList = model.getSortedFilteredPersonList();

        boolean isAnyPersonModified = false;
        String deletedMeetings = "";

        for (int i = 0; i < lastShownList.size(); i++) {
            Person person = lastShownList.get(i);
            boolean isModified = false;
            List<Meeting> toDelete = findMeetingstoDelete(person, currentTime);
            if (!toDelete.isEmpty()) {
                isModified = true;
                isAnyPersonModified = true;
            }
            if (isModified) {
                deletedMeetings += constructStringofMeetings(i + 1, person, toDelete);
                Person newPerson = deleteMeetings(person, toDelete);
                newPerson.updateSortedMeetingList(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR);
                model.setPerson(person, newPerson);
            }
        }
        if (isAnyPersonModified) {
            return new CommandResult(String.format("%s\n%s\n%s", REFRESH_SUCCESS,
                    "The following meeting(s) is(are) deleted", deletedMeetings));
        } else {
            return new CommandResult(REFRESH_NO_MODIFICATION);
        }
    }

    private List<Meeting> findMeetingstoDelete(Person person, LocalDateTime currentTime) {
        List<Meeting> meetingShownList = person.getFilteredMeetings();
        List<Meeting> toDelete = new ArrayList<>();
        for (Meeting meeting : meetingShownList) {
            if (meeting.getStartDate().getDateTime().isBefore(currentTime)) {
                toDelete.add(meeting);
            }
        }
        return toDelete;
    }

    private String constructStringofMeetings(int index, Person person, List<Meeting> meetings) {
        String result = "";
        for (Meeting meeting : meetings) {
            result += person.getName() + String.format(" (Index: %d) %s\n", index, meeting);
        }
        return result;
    }

    private Person deleteMeetings(Person person, List<Meeting> toDelete) {
        Person newPerson = PersonUtil.copyPerson(person);
        for (Meeting meeting : toDelete) {
            newPerson.removeMeeting(meeting);
        }
        return newPerson;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RefreshCommand;
    }

    @Override
    public String toString() {
        return "Refresh";
    }
}
