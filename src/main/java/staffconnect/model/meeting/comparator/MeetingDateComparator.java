package staffconnect.model.meeting.comparator;

import java.util.Comparator;

import staffconnect.model.meeting.Meeting;

/**
 * Represents a Comparator for a Meeting's Date in the staff book.
 */
public class MeetingDateComparator implements Comparator<Meeting> {

    public static final MeetingDateComparator MEETING_DATE_COMPARATOR = new MeetingDateComparator();

    @Override
    public int compare(Meeting meet, Meeting otherMeet) {

        return meet.getStartDate().getDateTime().compareTo(otherMeet.getStartDate().getDateTime());

    }
}
