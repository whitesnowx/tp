package staffconnect.logic.parser;

import static staffconnect.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static staffconnect.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static staffconnect.logic.parser.CliSyntax.PREFIX_MEETING_INDEX;
import static staffconnect.logic.parser.CommandParserTestUtil.assertParseFailure;
import static staffconnect.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static staffconnect.testutil.TypicalIndexes.INDEX_FIRST_MEETING;
import static staffconnect.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import staffconnect.logic.Messages;
import staffconnect.logic.commands.DeleteMeetingCommand;

class DeleteMeetingCommandParserTest {
    private static final DeleteMeetingCommandParser PARSER = new DeleteMeetingCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteMeetingCommand() {
        assertParseSuccess(PARSER, "1 " + PREFIX_MEETING_INDEX + "1",
                new DeleteMeetingCommand(INDEX_FIRST_PERSON, INDEX_FIRST_MEETING));
    }

    @Test
    public void parse_invalidMissingArgs_throwsParseException() {

        //Both args missing
        assertParseFailure(PARSER, PREFIX_MEETING_INDEX.toString(),
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMeetingCommand.MESSAGE_USAGE));
        //Missing person index
        assertParseFailure(PARSER, PREFIX_MEETING_INDEX + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMeetingCommand.MESSAGE_USAGE));
        //Missing meeting index
        assertParseFailure(PARSER, "1 " + PREFIX_MEETING_INDEX,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {

        //non empty preamble
        assertParseFailure(PARSER, PREAMBLE_NON_EMPTY + PREFIX_MEETING_INDEX + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMeetingCommand.MESSAGE_USAGE));

        //Invalid person index
        assertParseFailure(PARSER, "a " + PREFIX_MEETING_INDEX + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMeetingCommand.MESSAGE_USAGE));

        //invalid meeting index
        assertParseFailure(PARSER, "1 " + PREFIX_MEETING_INDEX + "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMeetingCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDuplicateArgs_failure() {
        //repeated valid meeting index
        String userInput = "1 " + PREFIX_MEETING_INDEX + "1 " + PREFIX_MEETING_INDEX + "1";
        assertParseFailure(PARSER, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEETING_INDEX));

        //repeated valid meeting index, one invalid meeting index
        userInput = "1 " + PREFIX_MEETING_INDEX + "1 " + PREFIX_MEETING_INDEX + "asd";
        assertParseFailure(PARSER, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEETING_INDEX));

        //repeated valid person index
        userInput = "1 " + "  1 " + PREFIX_MEETING_INDEX + "1";
        assertParseFailure(PARSER, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMeetingCommand.MESSAGE_USAGE));

        //repeated valid person index, one invalid person index
        userInput = "1 " + "asda " + PREFIX_MEETING_INDEX + "1";
        assertParseFailure(PARSER, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMeetingCommand.MESSAGE_USAGE));
    }

}
