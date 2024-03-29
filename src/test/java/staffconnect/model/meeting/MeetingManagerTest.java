package staffconnect.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.logic.commands.CommandTestUtil.VALID_KEYWORD_FINALS;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING_FINALS;
import static staffconnect.testutil.Assert.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import staffconnect.testutil.MeetingBookBuilder;

class MeetingManagerTest {
    private MeetingManager meetingManager = new MeetingManager();

    @Test
    public void constructor() {
        assertEquals(new MeetingBook(), meetingManager.getMeetingBook());
    }

    @Test
    public void hasMeeting_nullMeeting_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> meetingManager.hasMeeting(null));
    }

    @Test
    public void hasMeeting_meetingNotInMeetingList_returnsFalse() {
        assertFalse(meetingManager.hasMeeting(VALID_MEETING));
    }

    @Test
    public void hasMeeting_meetingInMeetingList_returnsTrue() {
        meetingManager.addMeeting(VALID_MEETING);
        assertTrue(meetingManager.hasMeeting(VALID_MEETING));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> meetingManager.getFilteredMeetingList().remove(0));
    }

    @Test
    public void equals() {
        MeetingBook meetingBook =
                new MeetingBookBuilder().withMeeting(VALID_MEETING).withMeeting(VALID_MEETING_FINALS).build();
        MeetingBook differentMeetingBook = new MeetingBook();


        // same values -> returns true
        meetingManager = new MeetingManager(meetingBook);
        MeetingManager meetingManagerCopy = new MeetingManager(meetingBook);
        assertEquals(meetingManager, meetingManagerCopy);

        // same object -> returns true
        assertEquals(meetingManager, meetingManager);

        // null -> returns false
        assertNotEquals(null, meetingManager);

        // different types -> returns false
        assertNotEquals(5, meetingManager);

        // different UniqueMeetingList in MeetingManager -> returns false
        assertNotEquals(meetingManager, new MeetingManager(differentMeetingBook));

        // different filteredList -> returns false
        String[] keywords = new String[]{VALID_KEYWORD_FINALS};
        meetingManager.updateFilteredMeetingList(
                new MeetingDescriptionContainsKeywordPredicate(Arrays.asList(keywords)));
        assertNotEquals(meetingManager, new MeetingManager(meetingBook));

    }
}
