package staffconnect.model.meeting;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import staffconnect.commons.util.ToStringBuilder;

/**
 * Wraps all the meeting data at the Person level
 */
public class MeetingBook implements ReadOnlyMeetingBook {

    private final UniqueMeetingList meetings = new UniqueMeetingList();

    public MeetingBook() {}

    /**
     * Creates a MeetingBook using the Meetings in the {@code toBeCopied}
     */
    public MeetingBook(ReadOnlyMeetingBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * Replaces the contents of the meeting list with {@code meetings}.
     * {@code meetings} must not contain duplicate meetings.
     */
    public void setMeetings(List<Meeting> meetings) {
        this.meetings.setMeetings(meetings);
    }

    /**
     * Resets the existing data of this {@code MeetingBook} with {@code newData}.
     */
    public void resetData(ReadOnlyMeetingBook newData) {
        requireNonNull(newData);

        setMeetings(newData.getMeetingList());
    }

    /**
     * Returns true if a meeting with the same identity as {@code meeting} exists in the meeting book.
     */
    public boolean hasMeeting(Meeting meeting) {
        requireNonNull(meeting);
        return meetings.contains(meeting);
    }

    /**
     * Adds a meeting to the meeting book.
     * The meeting must not already exist in the meeting book.
     */
    public void addMeeting(Meeting toAdd) {
        meetings.add(toAdd);
    }

    /**
     * Removes {@code key} from this {@code MeetingBook}.
     * {@code key} must exist in the meeting book.
     */
    public void removeMeeting(Meeting key) {
        meetings.remove(key);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("meetings", meetings)
                .toString();
    }

    @Override
    public ObservableList<Meeting> getMeetingList() {
        return meetings.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MeetingBook)) {
            return false;
        }

        MeetingBook otherMeetingBook = (MeetingBook) other;
        return meetings.equals(otherMeetingBook.meetings);
    }

    @Override
    public int hashCode() {
        return meetings.hashCode();
    }
}
