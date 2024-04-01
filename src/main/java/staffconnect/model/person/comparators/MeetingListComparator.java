package staffconnect.model.person.comparators;

import static staffconnect.model.meeting.comparator.MeetingDateTimeComparator.MEETING_DATE_COMPARATOR;

import java.util.Comparator;

import staffconnect.model.meeting.Meeting;
import staffconnect.model.person.Person;



/**
 * Represents a Comparator for a Person's Earliest Meeting in the staff book.
 */
public class MeetingListComparator implements Comparator<Person> {

    public static final MeetingListComparator MEETING_LIST_COMPARATOR = new MeetingListComparator();

    @Override
    public int compare(Person p1, Person p2) {
        Meeting earliestMeeting1 = p1.getMeetings().stream().min(MEETING_DATE_COMPARATOR).orElse(null);
        Meeting earliestMeeting2 = p2.getMeetings().stream().min(MEETING_DATE_COMPARATOR).orElse(null);

        if ((earliestMeeting1 == null) && (earliestMeeting2 == null)) {
            return 0;
        } else if (earliestMeeting1 == (null)) {
            return 1;
        } else if (earliestMeeting2 == (null)) {
            return -1;
        }

        return MEETING_DATE_COMPARATOR.compare(earliestMeeting1, earliestMeeting2);
    }

    @Override
    public String toString() {
        return "Earliest Meeting";
    }
}
