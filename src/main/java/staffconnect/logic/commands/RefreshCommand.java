package staffconnect.logic.commands;

import staffconnect.logic.commands.exceptions.CommandException;
import staffconnect.model.Model;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.person.Person;
import staffconnect.model.person.PersonUtil;

import static staffconnect.model.meeting.comparator.MeetingDateThenDescriptionComparator.MEETING_DATE_THEN_DESCRIPTION_COMPARATOR;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class RefreshCommand extends Command {

    public static final String COMMAND_WORD = "refresh";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deltes all meetings that has already started.\n"
            + "Example: " + COMMAND_WORD;

    public static final String REFRESH_SUCCESS = "Upcoming meetings successfully refreshed. "
            + "All outdated meetings are deleted.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        LocalDateTime currentTime = LocalDateTime.now();

        List<Person> lastShownList = model.getSortedFilteredPersonList();

        for (int i = 0; i < lastShownList.size(); i++) {
            Person person = lastShownList.get(i);
            Person newPerson = PersonUtil.copyPerson(person);
            List<Meeting> meetingShownList = newPerson.getFilteredMeetings();
            List<Meeting> toDelete = new ArrayList<>();

            for (Meeting meeting : meetingShownList) {
                if (meeting.getStartDate().getDateTime().isBefore(currentTime)) {
                    toDelete.add(meeting);
                }
            }

            for (Meeting meeting : toDelete) {
                newPerson.removeMeeting(meeting);
            }
            newPerson.updateSortedMeetingList(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR);
            model.setPerson(person, newPerson);
        }

        return new CommandResult(REFRESH_SUCCESS);
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
