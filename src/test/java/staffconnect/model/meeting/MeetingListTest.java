package staffconnect.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING;
import static staffconnect.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import staffconnect.model.meeting.exception.DuplicateMeetingException;
import staffconnect.model.meeting.exception.MeetingNotFoundException;


class MeetingListTest {

    private final MeetingList meetingList = new MeetingList();

    @Test
    public void contains_nullMeeting_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> meetingList.contains(null));
    }

    @Test
    public void contains_meetingNotInList_returnsFalse() {
        assertFalse(meetingList.contains(VALID_MEETING));
    }

    @Test
    public void contains_meetingInList_returnsTrue() {
        meetingList.add(VALID_MEETING);
        assertTrue(meetingList.contains(VALID_MEETING));
    }

    @Test
    public void add_nullMeeting_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> meetingList.add(null));
    }

    @Test
    public void add_duplicateMeeting_throwsDuplicateMeetingException() {
        meetingList.add(VALID_MEETING);
        assertThrows(DuplicateMeetingException.class, () -> meetingList.add(VALID_MEETING));
    }

    @Test
    public void remove_nullMeeting_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> meetingList.remove(null));
    }

    @Test
    public void remove_meetingDoesNotExist_throwsMeetingNotFoundException() {
        assertThrows(MeetingNotFoundException.class, () -> meetingList.remove(VALID_MEETING));
    }

    @Test
    public void remove_existingMeeting_removesMeeting() {
        meetingList.add(VALID_MEETING);
        meetingList.remove(VALID_MEETING);
        MeetingList expectedMeetingList = new MeetingList();
        assertEquals(expectedMeetingList, meetingList);
    }
    @Test
    public void setMeetings_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> meetingList.setMeetings((List<Meeting>) null));
    }

    @Test
    public void setMeetings_list_replacesOwnListWithProvidedList() {
        meetingList.add(VALID_MEETING);
        List<Meeting> anotherMeetingList = Collections.singletonList(VALID_MEETING);
        meetingList.setMeetings(anotherMeetingList);
        MeetingList expectedMeetingList = new MeetingList();
        expectedMeetingList.add(VALID_MEETING);
        assertEquals(expectedMeetingList, meetingList);
    }

    @Test
    public void setMeetings_listWithDuplicateMeetings_throwsDuplicateMeetingsException() {
        List<Meeting> listWithDuplicateMeetings = Arrays.asList(VALID_MEETING, VALID_MEETING);
        assertThrows(DuplicateMeetingException.class, () -> meetingList.setMeetings(listWithDuplicateMeetings));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> meetingList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(meetingList.asUnmodifiableObservableList().toString(), meetingList.toString());
    }

}
