package staffconnect.model.meeting.comparator;

import java.time.LocalDateTime;
import java.util.Comparator;

import staffconnect.model.meeting.Meeting;


/**
 * Represents a Comparator for a Meeting's MeetingDescription in the staff book.
 */
public class DescriptionComparator implements Comparator<Meeting> {

    public static final DescriptionComparator MEETING_DESCRIPTION_COMPARATOR = new DescriptionComparator();

    @Override
    public int compare(Meeting meet, Meeting otherMeet) {

        String firstDescription = meet.getDescription().toString();
        String secondDescription = otherMeet.getDescription().toString();
        LocalDateTime firstDateTime = meet.getStartDate().getDateTime();
        LocalDateTime secondDateTime = otherMeet.getStartDate().getDateTime();

        //Compares by time if equal
        return firstDescription.equals(secondDescription)
                ? firstDateTime.compareTo(secondDateTime) : firstDescription.compareTo(secondDescription);
    }
}
