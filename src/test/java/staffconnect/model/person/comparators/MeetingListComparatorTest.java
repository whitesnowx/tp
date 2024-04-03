package staffconnect.model.person.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.model.person.comparators.MeetingListComparator.MEETING_LIST_COMPARATOR;
import static staffconnect.model.person.comparators.NameComparator.NAME_COMPARATOR;
import static staffconnect.model.person.comparators.PhoneComparator.PHONE_COMPARATOR;
import static staffconnect.testutil.TypicalPersons.ALICE;
import static staffconnect.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;

import staffconnect.model.meeting.Meeting;
import staffconnect.model.meeting.MeetingDateTime;
import staffconnect.model.meeting.MeetingDescription;


public class MeetingListComparatorTest {
    @Test
    public void doesNotEquals() {
        assertNotEquals(MEETING_LIST_COMPARATOR, NAME_COMPARATOR);
        assertNotEquals(MEETING_LIST_COMPARATOR, PHONE_COMPARATOR);
    }

    @Test
    public void checkComparator() {
        Meeting meetingDay1A = new Meeting(new MeetingDescription("AA"), new MeetingDateTime("01/10/1111 11:11"));
        Meeting meetingDay1B = new Meeting(new MeetingDescription("BB"), new MeetingDateTime("01/10/1111 11:11"));
        Meeting meetingDay2A = new Meeting(new MeetingDescription("AA"), new MeetingDateTime("02/10/1111 11:11"));

        assert ALICE.getMeetings().isEmpty();
        assert BENSON.getMeetings().isEmpty();

        ALICE.addMeetings(meetingDay1A);
        BENSON.addMeetings(meetingDay1A);

        // same person (AA, 01/10/1111 11:11) = (AA, 01/10/1111 11:11)
        assertEquals(MEETING_LIST_COMPARATOR.compare(ALICE, ALICE), 0);

        // same meeting (AA, 01/10/1111 11:11) = (AA, 01/10/1111 11:11)
        assertEquals(MEETING_LIST_COMPARATOR.compare(ALICE, BENSON), 0);

        BENSON.removeMeeting(meetingDay1A);
        assert BENSON.getMeetings().isEmpty();
        BENSON.addMeetings(meetingDay1B);

        // same Date but different Description (AA, 01/10/1111 11:11) < (BB, 01/10/1111 11:11)
        assertTrue(MEETING_LIST_COMPARATOR.compare(ALICE, BENSON) < 0);
        // same Date but different Description (BB, 01/10/1111 11:11) > (AA, 01/10/1111 11:11)
        assertTrue(MEETING_LIST_COMPARATOR.compare(BENSON, ALICE) > 0);

        BENSON.removeMeeting(meetingDay1B);
        assert BENSON.getMeetings().isEmpty();
        BENSON.addMeetings(meetingDay2A);

        // same Description but different Date (AA, 01/10/1111 11:11) < (AA, 02/10/1111 11:11)
        assertTrue(MEETING_LIST_COMPARATOR.compare(ALICE, BENSON) < 0);
        // same Description but different Date (AA, 21/10/1111 11:11) > (AA, 01/10/1111 11:11)
        assertTrue(MEETING_LIST_COMPARATOR.compare(BENSON, ALICE) > 0);

        ALICE.removeMeeting(meetingDay1A);
        BENSON.removeMeeting(meetingDay2A);

        assert ALICE.getMeetings().isEmpty();
        assert BENSON.getMeetings().isEmpty();

        ALICE.addMeetings(meetingDay1B);
        BENSON.addMeetings(meetingDay2A);

        // Earlier Date but Larger alphabetical Description (BB, 01/10/1111 11:11) < (AA, 02/10/1111 11:11)
        assertTrue(MEETING_LIST_COMPARATOR.compare(ALICE, BENSON) < 0);
        // Later Date but Smaller alphabetical Description (BB, 01/10/1111 11:11) > (AA, 02/10/1111 11:11)
        assertTrue(MEETING_LIST_COMPARATOR.compare(BENSON, ALICE) > 0);

        ALICE.removeMeeting(meetingDay1B);
        BENSON.removeMeeting(meetingDay2A);

        assert ALICE.getMeetings().isEmpty();
        assert BENSON.getMeetings().isEmpty();
    }

    @Test
    public void toStringTest() {
        assertEquals(MEETING_LIST_COMPARATOR.toString(), "Earliest Meeting, Ascending alphanumeric Description");

        assertNotEquals(MEETING_LIST_COMPARATOR.toString(), "Name by alphanumerical order");
    }
}
