package staffconnect.model.meeting.comparator;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING_APRIL;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING_FINALS;
import static staffconnect.model.meeting.comparator.MeetingDateThenDescriptionComparator.MEETING_DATE_THEN_DESCRIPTION_COMPARATOR;
import static staffconnect.model.meeting.comparator.MeetingDateTimeComparator.MEETING_DATE_COMPARATOR;
import static staffconnect.model.meeting.comparator.MeetingDescriptionComparator.MEETING_DESCRIPTION_COMPARATOR;

import org.junit.jupiter.api.Test;

class MeetingDateThenDescriptionComparatorTest {

    @Test
    public void doesNotEquals() {
        assertNotEquals(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR, MEETING_DESCRIPTION_COMPARATOR);
        assertNotEquals(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR, MEETING_DATE_COMPARATOR);
    }

    @Test
    public void checkComparator() {

        // Different DateTime, Same Description
        // march < april
        assertTrue(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR.compare(VALID_MEETING, VALID_MEETING_APRIL) <= -1);
        // april > march
        assertTrue(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR.compare(VALID_MEETING_APRIL, VALID_MEETING) > -1);

        // Same DateTime, but different Description
        // VALID_MEETING has a longer description than VALID_MEETING_FINALS
        assertTrue(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR.compare(VALID_MEETING, VALID_MEETING_FINALS) > -1);
        assertTrue(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR.compare(VALID_MEETING_FINALS, VALID_MEETING) <= -1);

    }

}
