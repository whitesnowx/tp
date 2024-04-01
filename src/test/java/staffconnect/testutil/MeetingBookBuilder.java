package staffconnect.testutil;

import staffconnect.model.meeting.Meeting;
import staffconnect.model.meeting.MeetingBook;

/**
 * A utility class to help with building MeetingBook objects.
 * Example usage: <br>
 * {@code MeetingBook book = new MeetingBookBuilder().withMeeting(Meeting).build();}
 */
public class MeetingBookBuilder {

    private final MeetingBook meetingBook;

    public MeetingBookBuilder() {
        meetingBook = new MeetingBook();
    }

    /**
     * Adds a new {@code Meeting} to the {@code MeetingBook} that we are building.
     */
    public MeetingBookBuilder withMeeting(Meeting meeting) {
        meetingBook.addMeeting(meeting);
        return this;
    }

    public MeetingBook build() {
        return meetingBook;
    }
}
