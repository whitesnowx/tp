package staffconnect.model.meeting.exception;


/**
 * Signals that the operation will result in duplicate Meeting.
 */
public class DuplicateMeetingException extends RuntimeException {
    public DuplicateMeetingException() {
        super("Operation would result in duplicate meetings");
    }
}
