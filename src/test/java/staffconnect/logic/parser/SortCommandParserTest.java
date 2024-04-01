package staffconnect.logic.parser;


import static staffconnect.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static staffconnect.logic.parser.CliSyntax.PREFIX_FACULTY;
import static staffconnect.logic.parser.CliSyntax.PREFIX_MODULE;
import static staffconnect.logic.parser.CliSyntax.PREFIX_NAME;
import static staffconnect.logic.parser.CliSyntax.PREFIX_PHONE;
import static staffconnect.logic.parser.CliSyntax.PREFIX_VENUE;
import static staffconnect.logic.parser.CommandParserTestUtil.assertParseFailure;
import static staffconnect.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static staffconnect.model.person.comparators.FacultyComparator.FACULTY_COMPARATOR;
import static staffconnect.model.person.comparators.ModuleComparator.MODULE_COMPARATOR;
import static staffconnect.model.person.comparators.NameComparator.NAME_COMPARATOR;
import static staffconnect.model.person.comparators.PhoneComparator.PHONE_COMPARATOR;
import static staffconnect.model.person.comparators.VenueComparator.VENUE_COMPARATOR;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import staffconnect.logic.commands.SortCommand;
import staffconnect.model.person.Person;
import staffconnect.model.person.comparators.MultiComparator;


public class SortCommandParserTest {

    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {

        assertParseFailure(parser, "l/", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));


    }

    @Test
    public void parse_validArgs_returnsSortCommand() {
        List<Comparator<Person>> comparators = new ArrayList<>();
        comparators.add(VENUE_COMPARATOR);

        SortCommand expectedSortCommand = new SortCommand(new MultiComparator(comparators));

        // no leading and no trailing whitespaces
        assertParseSuccess(parser, "" + PREFIX_VENUE, expectedSortCommand);

        // 1 leading and no trailing whitespaces
        assertParseSuccess(parser, " " + PREFIX_VENUE, expectedSortCommand);

        // no leading and 1 trailing whitespaces
        assertParseSuccess(parser, PREFIX_VENUE + " ", expectedSortCommand);

        // multiple whitespaces before and after keywords
        assertParseSuccess(parser, "   " + PREFIX_VENUE + "    ", expectedSortCommand);


        comparators.add(NAME_COMPARATOR);
        expectedSortCommand = new SortCommand(new MultiComparator(comparators));

        // multiple whitespaces before and after keywords
        assertParseSuccess(parser, PREFIX_VENUE + " " + PREFIX_NAME, expectedSortCommand);

    }

    @Test
    public void parse_validArgs_returnsSortCorrectAttribute() {

        // Single Attribute
        assertParseSuccess(parser, "" + PREFIX_NAME,
                new SortCommand(new MultiComparator(List.of(NAME_COMPARATOR))));
        assertParseSuccess(parser, "" + PREFIX_PHONE,
                new SortCommand(new MultiComparator(List.of(PHONE_COMPARATOR))));
        assertParseSuccess(parser, "" + PREFIX_MODULE,
                new SortCommand(new MultiComparator(List.of(MODULE_COMPARATOR))));
        assertParseSuccess(parser, "" + PREFIX_FACULTY,
                new SortCommand(new MultiComparator(List.of(FACULTY_COMPARATOR))));
        assertParseSuccess(parser, "" + PREFIX_VENUE,
                new SortCommand(new MultiComparator(List.of(VENUE_COMPARATOR))));

        // Multiple Attribute
        assertParseSuccess(parser, PREFIX_VENUE + " " + PREFIX_FACULTY + " " + PREFIX_MODULE,
                new SortCommand(new MultiComparator(List.of(VENUE_COMPARATOR, FACULTY_COMPARATOR, MODULE_COMPARATOR))));


        assertParseSuccess(parser, PREFIX_FACULTY + " " + PREFIX_VENUE + " " + PREFIX_PHONE,
                new SortCommand(new MultiComparator(List.of(FACULTY_COMPARATOR, VENUE_COMPARATOR, PHONE_COMPARATOR))));
    }


}
