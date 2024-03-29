package staffconnect.model.meeting.comparator;

import java.util.Comparator;

import staffconnect.model.meeting.Meeting;

/**
 * Represents a Comparator for a Meeting's MeetingDescription in the staff book.
 */
public class MeetingDescriptionComparator implements Comparator<Meeting> {

    public static final MeetingDescriptionComparator MEETING_DESCRIPTION_COMPARATOR =
            new MeetingDescriptionComparator();

    @Override
    public int compare(Meeting meet, Meeting otherMeet) {

        return meet.getDescription().description.compareTo(otherMeet.getDescription().description);
    }
}
