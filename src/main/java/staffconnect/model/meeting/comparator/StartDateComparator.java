package staffconnect.model.meeting.comparator;

import java.time.LocalDateTime;
import java.util.Comparator;

import staffconnect.model.meeting.Meeting;

/**
 * Represents a Comparator for a Meeting's Date in the staff book.
 */
public class StartDateComparator implements Comparator<Meeting> {

    public static final StartDateComparator MEETING_DATE_COMPARATOR = new StartDateComparator();

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
