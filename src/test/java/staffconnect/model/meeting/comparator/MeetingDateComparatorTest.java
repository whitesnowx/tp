package staffconnect.model.meeting.comparator;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING_APRIL;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING_FINALS;
import static staffconnect.model.meeting.comparator.MeetingDateComparator.MEETING_DATE_COMPARATOR;
import static staffconnect.model.meeting.comparator.MeetingDescriptionComparator.MEETING_DESCRIPTION_COMPARATOR;

import org.junit.jupiter.api.Test;


public class MeetingDateComparatorTest {
    @Test
    public void doesNotEquals() {
        assertNotEquals(MEETING_DATE_COMPARATOR, MEETING_DESCRIPTION_COMPARATOR);
    }

    @Test
    public void checkComparator() {

        //Different time
        assertTrue(MEETING_DATE_COMPARATOR.compare(VALID_MEETING, VALID_MEETING_APRIL) <= -1); // march < april
        assertTrue(MEETING_DATE_COMPARATOR.compare(VALID_MEETING_APRIL, VALID_MEETING) > -1); // march

        //Same time but different description
        //valid meeting has a longer description than valid meeting finals
        assertTrue(MEETING_DATE_COMPARATOR.compare(VALID_MEETING, VALID_MEETING_FINALS) > -1); //longer desc
        assertTrue(MEETING_DATE_COMPARATOR.compare(VALID_MEETING_FINALS, VALID_MEETING) <= -1); //shorter desc
    }
}
