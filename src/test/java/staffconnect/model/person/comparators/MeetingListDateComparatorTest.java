package staffconnect.model.person.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.model.person.comparators.MeetingListDateComparator.MEETING_LIST_DATE_COMPARATOR;
import static staffconnect.model.person.comparators.NameComparator.NAME_COMPARATOR;
import static staffconnect.model.person.comparators.PhoneComparator.PHONE_COMPARATOR;
//import static staffconnect.testutil.TypicalPersons.ALICE;
//import static staffconnect.testutil.TypicalPersons.BENSON;
//import static staffconnect.testutil.TypicalPersons.CARL;
//import static staffconnect.testutil.TypicalPersons.DANIEL;
//import static staffconnect.testutil.TypicalPersons.ELLE;

import org.junit.jupiter.api.Test;

import staffconnect.model.meeting.Meeting;
import staffconnect.model.meeting.MeetingDateTime;
import staffconnect.model.meeting.MeetingDescription;


public class MeetingListDateComparatorTest {
    @Test
    public void doesNotEquals() {
        assertNotEquals(MEETING_LIST_DATE_COMPARATOR, NAME_COMPARATOR);
        assertNotEquals(MEETING_LIST_DATE_COMPARATOR, PHONE_COMPARATOR);
    }

    @Test
    public void checkComparator() {
        Meeting meetingDay1A = new Meeting(new MeetingDescription("Day1A"), new MeetingDateTime("01/10/1111 11:11"));
        Meeting meetingDay2A = new Meeting(new MeetingDescription("Day2A"), new MeetingDateTime("02/10/1111 11:11"));
        Meeting meetingDay1B = new Meeting(new MeetingDescription("Day1B"), new MeetingDateTime("01/10/1111 11:11"));
        Meeting meetingDay2B = new Meeting(new MeetingDescription("Day2B"), new MeetingDateTime("02/10/1111 11:11"));

        //ALICE.addMeetings(meetingDay1A); //A (1A), B, C, D, E
        //BENSON.addMeetings(meetingDay1A); //A (3), B (2), C, D, E
        //CARL.addMeetings(meetingDay1A); //A (3), B (2), C (4), D, E
        //
        //assertTrue(MEETING_LIST_DATE_COMPARATOR.compare(BENSON, ALICE) <= -1); // 02/10/1111 earlier than 03/10/1111
        //assertTrue(MEETING_LIST_DATE_COMPARATOR.compare(ALICE, BENSON) >= 1); // 03/10/1111 later than 02/10/1111
        //
        //assertTrue(MEETING_LIST_DATE_COMPARATOR.compare(CARL, ALICE) >= 1); // 04/10/1111 later than 03/10/1111
        //assertTrue(MEETING_LIST_DATE_COMPARATOR.compare(ALICE, CARL) <= -1); // 03/10/1111 earlier than 04/10/1111
        //
        //CARL.addMeetings(meetingDay1); //A (3), B (2), C (1, 4), D, E
        //assertTrue(MEETING_LIST_DATE_COMPARATOR.compare(CARL, ALICE) <= -1); // 01/10/1111 earlier than 03/10/1111
        //assertTrue(MEETING_LIST_DATE_COMPARATOR.compare(ALICE, CARL) >= 1); // 03/10/1111 later than 01/10/1111
        //
        //ALICE.addMeetings(meetingDay1); //A (1, 3), B (2), C (1, 4), D, E
        //assertEquals(MEETING_LIST_DATE_COMPARATOR.compare(CARL, ALICE), 0); // same date 02/10/1111
        //assertEquals(MEETING_LIST_DATE_COMPARATOR.compare(ALICE, CARL), 0); // same date 02/10/1111
        //assertEquals(MEETING_LIST_DATE_COMPARATOR.compare(ALICE, ALICE), 0); // same person
        //
        //assertTrue(MEETING_LIST_DATE_COMPARATOR.compare(ALICE, DANIEL) <= -1); // meeting < no meeting
        //assertTrue(MEETING_LIST_DATE_COMPARATOR.compare(DANIEL, ALICE) >= 1); // no meeting > meeting
        //assertEquals(MEETING_LIST_DATE_COMPARATOR.compare(DANIEL, ELLE), 0); // no meeting = no meeting
        //
        //Meeting meetingPreviousMonth = new Meeting(new MeetingDescription("PreviousMonth"),
        //        new MeetingDateTime("04/09/1111 11:11"));
        //Meeting meetingPreviousYear = new Meeting(new MeetingDescription("PreviousYear"),
        //        new MeetingDateTime("04/10/1110 11:11"));
        //
        //DANIEL.addMeetings(meetingPreviousMonth); //A (1, 3), B (2), C (1, 4), D (-1YEAR), E
        //assertTrue(MEETING_LIST_DATE_COMPARATOR.compare(ALICE, DANIEL) >= 1); // 04/10/1111 later than 04/09/1111
        //
        //ELLE.addMeetings(meetingPreviousYear); //A (1, 3), B (2), C (1, 4), D (-1MONTH), E (-1YEAR)
        //assertTrue(MEETING_LIST_DATE_COMPARATOR.compare(ALICE, ELLE) >= 1); // 04/10/1111 later than 04/10/1110
        //
        //assertTrue(MEETING_LIST_DATE_COMPARATOR.compare(DANIEL, ELLE) >= 1); // 04/09/1111 later than 04/10/1110
    }

    @Test
    public void toStringTest() {
        assertEquals(MEETING_LIST_DATE_COMPARATOR.toString(), "Earliest Meeting");

        assertNotEquals(MEETING_LIST_DATE_COMPARATOR.toString(), "Name by alphanumerical order");
    }
}
