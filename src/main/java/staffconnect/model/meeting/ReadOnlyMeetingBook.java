package staffconnect.model.meeting;

import javafx.collections.ObservableList;

/**
 * Unmodifiable view of a MeetingBook
 */
public interface ReadOnlyMeetingBook {
    /**
     * Returns an unmodifiable view of the MeetingBook.
     * This list will not contain any duplicate meetings.
     */
    ObservableList<Meeting> getMeetingList();
}
