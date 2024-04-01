package staffconnect.model.meeting;

import static java.util.Objects.requireNonNull;
import static staffconnect.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Meeting's starting time in the staff book.
 * Guarantees: immutable; is valid as declared in {@link #isValidMeetDateTime(String)}
 */
public class MeetingDateTime {

    public static final String MESSAGE_CONSTRAINTS = "DateTime should be of the correct format and values dd/mm/yyyy "
            + "HH:mm";
    public static final String VALIDATION_REGEX = "\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}";

    private static final DateTimeFormatter PROCESS_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(java.time.format.ResolverStyle.STRICT);

    private final LocalDateTime value;

    /**
     * Constructs a {@code MeetingDateTime}.
     *
     * @param dateTime A valid dateTime.
     */
    public MeetingDateTime(String dateTime) {
        requireNonNull(dateTime);
        checkArgument(isValidMeetDateTime(dateTime), MESSAGE_CONSTRAINTS);
        value = java.time.LocalDateTime.parse(dateTime, PROCESS_FORMAT);
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidMeetDateTime(String test) {
        return test.matches(VALIDATION_REGEX) && isParsable(test);
    }

    //Wrapper method only unique to this class
    private static boolean isParsable(String test) {
        try {
            LocalDateTime.parse(test, PROCESS_FORMAT);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public LocalDateTime getDateTime() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MeetingDateTime)) {
            return false;
        }

        MeetingDateTime otherDate = (MeetingDateTime) other;
        return value.equals(otherDate.value);
    }

    @Override
    public String toString() {
        return value.format(PROCESS_FORMAT);
    }

}
