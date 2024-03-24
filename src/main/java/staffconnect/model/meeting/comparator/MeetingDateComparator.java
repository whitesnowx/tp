package staffconnect.model.meeting.comparator;

import java.time.LocalDateTime;
import java.util.Comparator;

import staffconnect.model.meeting.Meeting;

/**
 * Represents a Comparator for a Meeting's Date in the staff book.
 */
public class MeetingDateComparator implements Comparator<Meeting> {

    public static final MeetingDateComparator MEETING_DATE_COMPARATOR = new MeetingDateComparator();

    @Override
    public int compare(Meeting meet, Meeting otherMeet) {

        String firstDescription = meet.getDescription().toString();
        String secondDescription = otherMeet.getDescription().toString();
        LocalDateTime firstDateTime = meet.getStartDate().getDateTime();
        LocalDateTime secondDateTime = otherMeet.getStartDate().getDateTime();

        //Compares by description if equal
        return firstDateTime.equals(secondDateTime)
                ? firstDescription.compareTo(secondDescription) : firstDateTime.compareTo(secondDateTime);
    }
}
