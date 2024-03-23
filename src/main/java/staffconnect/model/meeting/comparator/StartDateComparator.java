package staffconnect.model.meeting.comparator;

import java.util.Comparator;

import staffconnect.model.meeting.Meeting;

/**
 * Represents a Comparator for a Meeting's Date in the staff book.
 */
public class StartDateComparator implements Comparator<Meeting> {

    public static final StartDateComparator MEETING_DATE_COMPARATOR = new StartDateComparator();

    @Override
    public int compare(Meeting meet, Meeting otherMeet) {
        return meet.getStartDate().getDateTime().compareTo(otherMeet.getStartDate().getDateTime());
    }
}
