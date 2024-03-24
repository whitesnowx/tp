package staffconnect.testutil;

import staffconnect.model.meeting.Meeting;
import staffconnect.model.meeting.MeetingList;

/**
 * A utility class to help with building MeetingList objects.
 * Example usage: <br>
 *     {@code MeetingList ab = new MeetingListBuilder().withMeeting(Meeting).build();}
 */
public class MeetingListBuilder {

    private MeetingList meetingList;

    public MeetingListBuilder() {
        meetingList = new MeetingList();
    }

    public MeetingListBuilder(MeetingList meetingList) {
        this.meetingList = meetingList;
    }

    /**
     * Adds a new {@code Meeting} to the {@code MeetingList} that we are building.
     */
    public MeetingListBuilder withMeeting(Meeting meeting) {
        meetingList.add(meeting);
        return this;
    }

    public MeetingList build() {
        return meetingList;
    }
}
