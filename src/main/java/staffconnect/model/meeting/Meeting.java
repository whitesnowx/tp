package staffconnect.model.meeting;


import static staffconnect.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a Meeting event in the staff book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Meeting {

    private final MeetingDescription description;
    private final MeetingDateTime startDate;

    /**
     * Constructs a {@code Meeting}.
     *
     * @param description A valid meeting description.
     * @param startDate   A valid time and date for the meeting.
     */

    public Meeting(MeetingDescription description, MeetingDateTime startDate) {
        requireAllNonNull(description, startDate);
        this.description = description;
        this.startDate = startDate;
    }

    public MeetingDescription getDescription() {
        return description;
    }

    public MeetingDateTime getStartDate() {
        return startDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, startDate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Meeting)) {
            return false;
        }

        Meeting otherMeeting = (Meeting) other;
        return description.equals(otherMeeting.description) && startDate.equals(otherMeeting.startDate);
    }

    /**
     * Formats state as text for viewing.
     */
    public String toString() {
        return startDate + ":" + description;
    }



}
