package staffconnect.model.meeting;

import java.util.Comparator;

/**
 * Represents a Comparator for a Meeting's Description in the staff book.
 */
public class DescriptionComparator implements Comparator<Meeting> {

    public static final DescriptionComparator MEETING_DESCRIPTION_COMPARATOR = new DescriptionComparator();

    @Override
    public int compare(Meeting meet, Meeting otherMeet) {
        return meet.getDescription().toString().compareTo(otherMeet.getDescription().toString());
    }
}
