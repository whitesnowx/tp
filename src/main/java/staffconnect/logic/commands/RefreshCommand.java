package staffconnect.logic.commands;

import staffconnect.logic.commands.exceptions.CommandException;
import staffconnect.model.Model;

public class RefreshCommand extends Command {

    public static final String COMMAND_WORD = "refresh";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return null;
    }
}
