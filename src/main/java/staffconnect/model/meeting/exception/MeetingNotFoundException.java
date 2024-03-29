package staffconnect.model.meeting.exception;

/**
 * Signals that the operation is unable to find the specified meeting.
 */
public class MeetingNotFoundException extends RuntimeException {
    public MeetingNotFoundException() {
        super("Operation cannot continue as meeting does not exist");
    }
}
