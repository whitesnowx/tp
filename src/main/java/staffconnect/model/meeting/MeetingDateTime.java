package staffconnect.model.meeting;

import static java.util.Objects.requireNonNull;
import static staffconnect.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

/**
 * Represents a Meeting's starting time in the staff book.
 * Guarantees: immutable; is valid as declared in {@link #isValidMeetDateTime(String)}
 */
public class MeetingDateTime {

    public static final String MESSAGE_CONSTRAINTS = "DateTime should be of the correct format and values\n"
            + "accepted formats for dates include yyyy-MM-dd, yyyy-M-d, dd-MM-yyyy, yyyy-MM-d, "
            + "d-MM-yyyy, d-M-yyyy, dd-M-yyyy, d/MM/yyyy, d-M-yyyy, dd-M-yyyy, dd/MM/yyyy, "
            + "yyyy/MM/dd, yyyy/MM/d, yyyy/M/dd, yyyy/M/d\n"
            + "accepted formats for time include HH:mm, H:mm, HHmm";
    public static final String VALIDATION_REGEX = "\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}";

    private static final List<String> DATE_FORMATS = List.of(
            // Separators: -
            "d-M-uuuu", // d-M-yyyy
            "dd-M-uuuu", // dd-M-yyyy
            "d-MM-uuuu", // d-MM-yyyy
            "dd-MM-uuuu", // dd-MM-yyyy
            "uuuu-M-d", // yyyy-M-d
            "uuuu-MM-d", // yyyy-MM-d
            "uuuu-MM-dd", // yyyy-MM-dd

            // Separators: /
            "d/M/uuuu", // d/M/yyyy
            "dd/M/uuuu", // dd/M/yyyy
            "d/MM/uuuu", // d/MM/yyyy
            "dd/MM/uuuu", // dd/MM/yyyy
            "uuuu/M/d", // yyyy/M/d
            "uuuu/M/dd", // yyyy/M/dd
            "uuuu/MM/d", // yyyy/MM/d
            "uuuu/MM/dd" // yyyy/MM/dd
    );

    private static final List<String> TIME_FORMATS = List.of(
            "HH:mm",
            "H:mm",
            "HHmm"
    );

    private static final List<String> DATE_REGEXS = List.of(
            // Separators: -
            "\\d{1,2}-\\d{1,2}-\\d{4}", // d-M-yyyy
            "\\d{2}-\\d{1,2}-\\d{4}", // dd-M-yyyy
            "\\d{1,2}-\\d{2}-\\d{4}", // d-MM-yyyy
            "\\d{2}-\\d{2}-\\d{4}", // dd-MM-yyyy
            "\\d{4}-\\d{1,2}-\\d{1,2}", // yyyy-M-d
            "\\d{4}-\\d{2}-\\d{1,2}", // yyyy-MM-d
            "\\d{4}-\\d{2}-\\d{2}", // yyyy-MM-dd

            // Separators: /
            "\\d{1,2}/\\d{1,2}/\\d{4}", // d/M/yyyy
            "\\d{2}/\\d{1,2}/\\d{4}", // dd/M/yyyy
            "\\d{1,2}/\\d{2}/\\d{4}", // d/MM/yyyy
            "\\d{2}/\\d{2}/\\d{4}", // dd/MM/yyyy
            "\\d{4}/\\d{1,2}/\\d{1,2}", // yyyy/M/d
            "\\d{4}/\\d{1,2}/\\d{2}", // yyyy/M/dd
            "\\d{4}/\\d{2}/\\d{1,2}", // yyyy/MM/d
            "\\d{4}/\\d{2}/\\d{2}" // yyyy/MM/dd
    );

    private static final List<String> TIME_REGEXS = List.of(
            "\\d{2}:\\d{2}", // HH:mm
            "\\d{1,2}:\\d{2}", // H:mm
            "\\d{2}\\d{2}" // HHmm
    );

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
        value = parse(dateTime);
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidMeetDateTime(String test) {
        for (int i = 0; i < DATE_FORMATS.size(); i++) {
            for (int j = 0; j < TIME_FORMATS.size(); j++) {
                String regex = DATE_REGEXS.get(i) + " " + TIME_REGEXS.get(j);
                String format = DATE_FORMATS.get(i) + " " + TIME_FORMATS.get(j);
                if (test.matches(regex) && isParsable(test, format)) {
                    return true;
                }
            }
        }
        return false;
    }

    //Wrapper method only unique to this class
    private static boolean isParsable(String test, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format)
                    .withLocale(Locale.ENGLISH)
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT);
            LocalDateTime.parse(test, formatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    private static LocalDateTime parse(String date) {
        for (String dateFormat : DATE_FORMATS) {
            for (String timeFormat : TIME_FORMATS) {
                String format = dateFormat + " " + timeFormat;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format)
                        .withLocale(Locale.ENGLISH)
                        .withResolverStyle(java.time.format.ResolverStyle.STRICT);
                try {
                    return java.time.LocalDateTime.parse(date, formatter);
                } catch (DateTimeParseException e) {
                    continue;
                }
            }
        }
        throw new IllegalArgumentException();
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
