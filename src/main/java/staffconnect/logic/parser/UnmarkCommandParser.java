package staffconnect.logic.parser;

import static staffconnect.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import staffconnect.commons.core.index.Index;
import staffconnect.logic.commands.UnmarkCommand;
import staffconnect.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnmarkCommand object
 */
public class UnmarkCommandParser implements Parser<UnmarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the UnmarkCommand
     * and returns a UnmarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnmarkCommand parse(String args) throws ParseException {
        Index index;
        try {
            index = ParserUtil.parseIndex(args);
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE), pe);
        }
        return new UnmarkCommand(index);
    }
}
