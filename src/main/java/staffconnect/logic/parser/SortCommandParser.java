package staffconnect.logic.parser;

import static staffconnect.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static staffconnect.logic.parser.CliSyntax.PREFIX_FACULTY;
import static staffconnect.logic.parser.CliSyntax.PREFIX_MEETING_STARTDATE;
import static staffconnect.logic.parser.CliSyntax.PREFIX_MODULE;
import static staffconnect.logic.parser.CliSyntax.PREFIX_NAME;
import static staffconnect.logic.parser.CliSyntax.PREFIX_PHONE;
import static staffconnect.logic.parser.CliSyntax.PREFIX_VENUE;
import static staffconnect.model.person.comparators.FacultyComparator.FACULTY_COMPARATOR;
import static staffconnect.model.person.comparators.MeetingListDateComparator.MEETING_LIST_DATE_COMPARATOR;
import static staffconnect.model.person.comparators.ModuleComparator.MODULE_COMPARATOR;
import static staffconnect.model.person.comparators.NameComparator.NAME_COMPARATOR;
import static staffconnect.model.person.comparators.PhoneComparator.PHONE_COMPARATOR;
import static staffconnect.model.person.comparators.VenueComparator.VENUE_COMPARATOR;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import staffconnect.logic.commands.SortCommand;
import staffconnect.logic.parser.exceptions.ParseException;
import staffconnect.model.person.Person;
import staffconnect.model.person.comparators.MultiComparator;

/**
 * Parses input arguments and creates a new SortCommand object
 */
public class SortCommandParser implements Parser<SortCommand> {

    private static final Logger logger = Logger.getLogger(SortCommandParser.class.getName());

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns a SortCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        logger.info("Parsing SortCommand with arguments: " + args);
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            logger.warning("Empty argument provided.");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        return new SortCommand(new MultiComparator(parseComparatorsForKeywords(trimmedArgs)));
    }

    private List<Comparator<Person>> parseComparatorsForKeywords(String keywords) throws ParseException {
        String[] keywordsArray = keywords.split("\\s+");

        List<Comparator<Person>> comparators = new ArrayList<>();
        for (String keyword : keywordsArray) {
            comparators.add(parseComparatorForKeyword(keyword));
        }

        return comparators;
    }

    private Comparator<Person> parseComparatorForKeyword(String keyword) throws ParseException {
        logger.info("Parsing comparator for keyword: " + keyword);
        if (keyword.equals(PREFIX_NAME.getPrefix())) {
            logger.fine("Using NameComparator.");
            return NAME_COMPARATOR;
        } else if (keyword.equals(PREFIX_PHONE.getPrefix())) {
            logger.fine("Using PhoneComparator.");
            return PHONE_COMPARATOR;
        } else if (keyword.equals(PREFIX_MODULE.getPrefix())) {
            logger.fine("Using ModuleComparator.");
            return MODULE_COMPARATOR;
        } else if (keyword.equals(PREFIX_FACULTY.getPrefix())) {
            logger.fine("Using FacultyComparator.");
            return FACULTY_COMPARATOR;
        } else if (keyword.equals(PREFIX_VENUE.getPrefix())) {
            logger.fine("Using VenueComparator.");
            return VENUE_COMPARATOR;
        } else if (keyword.equals(PREFIX_MEETING_STARTDATE.getPrefix())) {
            logger.fine("Using MeetingListComparator.");
            return MEETING_LIST_DATE_COMPARATOR;
        } else {
            logger.warning("Invalid command format.");
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

    }


}
