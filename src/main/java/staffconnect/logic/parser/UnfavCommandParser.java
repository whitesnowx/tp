package staffconnect.logic.parser;

import static staffconnect.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import staffconnect.commons.core.index.Index;
import staffconnect.logic.commands.UnfavCommand;
import staffconnect.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnfavCommand object
 */
public class UnfavCommandParser implements Parser<UnfavCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnfavCommand
     * and returns a UnfavCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnfavCommand parse(String args) throws ParseException {
        Index index;
        try {
            index = ParserUtil.parseIndex(args);
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnfavCommand.MESSAGE_USAGE), pe);
        }
        return new UnfavCommand(index);
    }
}
