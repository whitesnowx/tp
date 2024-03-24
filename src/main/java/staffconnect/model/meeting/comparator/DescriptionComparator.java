package staffconnect.model.meeting.comparator;

import java.util.Comparator;

import staffconnect.model.meeting.Meeting;

/**
 * Represents a Comparator for a Meeting's MeetingDescription in the staff book.
 */
public class DescriptionComparator implements Comparator<Meeting> {

    public static final DescriptionComparator MEETING_DESCRIPTION_COMPARATOR = new DescriptionComparator();

    @Override
    public int compare(Meeting meet, Meeting otherMeet) {
        return meet.getDescription().toString().compareTo(otherMeet.getDescription().toString());
    }
}
