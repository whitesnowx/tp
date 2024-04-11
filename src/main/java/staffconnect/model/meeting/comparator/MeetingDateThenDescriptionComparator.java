package staffconnect.model.meeting.comparator;

import static staffconnect.model.meeting.comparator.MeetingDateTimeComparator.MEETING_DATE_COMPARATOR;
import static staffconnect.model.meeting.comparator.MeetingDescriptionComparator.MEETING_DESCRIPTION_COMPARATOR;

import java.util.Comparator;

import staffconnect.model.meeting.Meeting;

/**
 * Represents a Comparator for comparing Meeting's Date then by Description if Dates are equal.
 */
public class MeetingDateThenDescriptionComparator implements Comparator<Meeting> {

    public static final MeetingDateThenDescriptionComparator MEETING_DATE_THEN_DESCRIPTION_COMPARATOR =
            new MeetingDateThenDescriptionComparator();

    @Override
    public int compare(Meeting meet, Meeting otherMeet) {

        //Compares by Description if DateTime is equal.
        return MEETING_DATE_COMPARATOR.compare(meet, otherMeet) == 0
                ? MEETING_DESCRIPTION_COMPARATOR.compare(meet, otherMeet)
                : MEETING_DATE_COMPARATOR.compare(meet, otherMeet);
    }
}
