package staffconnect.model.person.comparators;

import static staffconnect.model.meeting.comparator.MeetingDateThenDescriptionComparator.MEETING_DATE_THEN_DESCRIPTION_COMPARATOR;

import java.util.Comparator;

import staffconnect.model.meeting.Meeting;
import staffconnect.model.person.Person;


/**
 * Represents a Comparator for a Person's Earliest Meeting followed by the ascending description in the staff book.
 */
public class MeetingListComparator implements Comparator<Person> {

    public static final MeetingListComparator MEETING_LIST_COMPARATOR = new MeetingListComparator();

    @Override
    public int compare(Person p1, Person p2) {
        Meeting earliestAlphanumericMeeting1 = p1.getMeetings().stream().min(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR).orElse(null);
        Meeting earliestAlphanumericMeeting2 = p2.getMeetings().stream().min(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR).orElse(null);

        if ((earliestAlphanumericMeeting1 == null) && (earliestAlphanumericMeeting2 == null)) {
            return 0;
        } else if (earliestAlphanumericMeeting1 == (null)) {
            return 1;
        } else if (earliestAlphanumericMeeting2 == (null)) {
            return -1;
        }

        return MEETING_DATE_THEN_DESCRIPTION_COMPARATOR.compare(earliestAlphanumericMeeting1, earliestAlphanumericMeeting2);
    }

    @Override
    public String toString() {
        return "Earliest Meeting, Ascending alphanumeric Description";
    }
}
