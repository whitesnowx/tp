package staffconnect.logic.parser;

import static staffconnect.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static staffconnect.logic.parser.CommandParserTestUtil.assertParseFailure;
import static staffconnect.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static staffconnect.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import staffconnect.logic.commands.UnfavCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the UnfavCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the UnfavCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class UnfavCommandParserTest {

    private UnfavCommandParser parser = new UnfavCommandParser();

    @Test
    public void parse_validArgs_returnsUnmarkCommand() {
        assertParseSuccess(parser, "1", new UnfavCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnfavCommand.MESSAGE_USAGE));
    }
}
