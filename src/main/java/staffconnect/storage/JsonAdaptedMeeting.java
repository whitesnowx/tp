package staffconnect.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import staffconnect.commons.exceptions.IllegalValueException;
import staffconnect.model.meeting.MeetingDateTime;
import staffconnect.model.meeting.MeetingDescription;
import staffconnect.model.meeting.Meeting;

/**
 * Jackson-friendly version of {@link Meeting}.
 */
class JsonAdaptedMeeting {

    private final String description;

    private final String date;

    /**
     * Constructs a {@code JsonAdaptedMeeting} with the given {@code Meeting}.
     */
    @JsonCreator
    public JsonAdaptedMeeting(@JsonProperty("description") String description, @JsonProperty("date") String date) {
        this.description = description;
        this.date = date;
    }

    /**
     * Converts a given {@code Meeting} into this class for Jackson use.
     */
    public JsonAdaptedMeeting(Meeting source) {
        description = source.getDescription().toString();
        date = source.getStartDate().toString();

    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Meeting} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted meeting.
     */
    public Meeting toModelType() throws IllegalValueException {
        if (!MeetingDescription.isValidDescription(description)) {
            throw new IllegalValueException(MeetingDescription.MESSAGE_CONSTRAINTS);
        }
        if (!MeetingDateTime.isValidMeetDateTime(date)) {
            throw new IllegalValueException(MeetingDateTime.MESSAGE_CONSTRAINTS);
        }
        return new Meeting(new MeetingDescription(description), new MeetingDateTime(date));
    }

}
