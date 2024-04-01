package staffconnect.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING_APRIL;
import static staffconnect.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import staffconnect.model.meeting.exception.DuplicateMeetingException;
import staffconnect.testutil.MeetingBookBuilder;

class MeetingBookTest {

    private final MeetingBook meetingBook = new MeetingBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), meetingBook.getMeetingList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> meetingBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyMeetingBook_replacesData() {
        MeetingBook newData = new MeetingBookBuilder().withMeeting(VALID_MEETING).build();
        meetingBook.resetData(newData);
        assertEquals(newData, meetingBook);
    }

    @Test
    public void resetData_withDuplicateMeetings_throwsDuplicateMeetingException() {

        List<Meeting> newMeetings = Arrays.asList(VALID_MEETING, VALID_MEETING);
        MeetingBookStub newData = new MeetingBookStub(newMeetings);

        assertThrows(DuplicateMeetingException.class, () -> meetingBook.resetData(newData));
    }

    @Test
    public void hasMeeting_nullMeeting_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> meetingBook.hasMeeting(null));
    }

    @Test
    public void hasMeeting_meetingNotInMeetingBook_returnsFalse() {
        assertFalse(meetingBook.hasMeeting(VALID_MEETING_APRIL));
    }

    @Test
    public void hasMeeting_meetingInMeetingBook_returnsTrue() {
        meetingBook.addMeeting(VALID_MEETING);
        assertTrue(meetingBook.hasMeeting(VALID_MEETING));
    }

    @Test
    public void getMeetingList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> meetingBook.getMeetingList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = MeetingBook.class.getCanonicalName() + "{meetings=" + meetingBook.getMeetingList() + "}";
        assertEquals(expected, meetingBook.toString());
    }

    /**
     * A stub ReadOnlyMeetingBook whose meeting list can violate interface constraints.
     */
    private static class MeetingBookStub implements ReadOnlyMeetingBook {
        private final ObservableList<Meeting> meetings = FXCollections.observableArrayList();

        MeetingBookStub(Collection<Meeting> meetings) {
            this.meetings.setAll(meetings);
        }

        @Override
        public ObservableList<Meeting> getMeetingList() {
            return meetings;
        }
    }

}
