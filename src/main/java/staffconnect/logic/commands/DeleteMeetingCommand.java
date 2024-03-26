package staffconnect.logic.commands;

import static java.util.Objects.requireNonNull;
import static staffconnect.logic.parser.CliSyntax.PREFIX_MEETING_INDEX;

import java.util.List;

import staffconnect.commons.core.index.Index;
import staffconnect.commons.util.ToStringBuilder;
import staffconnect.logic.Messages;
import staffconnect.logic.commands.exceptions.CommandException;
import staffconnect.model.Model;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.person.Person;

/**
 * Deletes a meeting identified using its displayed index from the person's meeting list.
 */
public class DeleteMeetingCommand extends Command {

    public static final String COMMAND_WORD = "meeting-delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes the meeting identified in the person's "
            + "meeting list. \n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_MEETING_INDEX + "INDEX (must be a positive integer) \n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_MEETING_INDEX + "2";

    public static final String MESSAGE_DELETE_MEETING_SUCCESS = "Deleted Meeting: %1$s";

    private final Index targetPersonIndex;

    private final Index targetMeetingIndex;

    /**
     * Constructs a DeleteMeetingCommand to indicate which meeting to delete from the person's meeting list.
     * @param targetPersonIndex to select the person's meeting list.
     * @param targetMeetingIndex to select the meeting to delete.
     */
    public DeleteMeetingCommand(Index targetPersonIndex, Index targetMeetingIndex) {
        this.targetPersonIndex = targetPersonIndex;
        this.targetMeetingIndex = targetMeetingIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetPersonIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToSelect = lastShownList.get(targetPersonIndex.getZeroBased());
        List<Meeting> meetingShownList = personToSelect.getFilteredMeetings();

        // can't group guard clauses together, as this guard clause has to be here
        if (targetMeetingIndex.getZeroBased() >= meetingShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MEETING_DISPLAYED_INDEX);
        }

        Meeting meetingToSelect = meetingShownList.get(targetMeetingIndex.getZeroBased());
        personToSelect.removeMeeting(meetingToSelect);

        //force update the ui
        model.setPerson(personToSelect, personToSelect);
        return new CommandResult(String.format(MESSAGE_DELETE_MEETING_SUCCESS, Messages.format(meetingToSelect)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteMeetingCommand)) {
            return false;
        }

        DeleteMeetingCommand otherDeleteCommand = (DeleteMeetingCommand) other;
        return targetPersonIndex.equals(otherDeleteCommand.targetPersonIndex)
                && targetMeetingIndex.equals(otherDeleteCommand.targetMeetingIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetPersonIndex", targetPersonIndex)
                .add("targetMeetingIndex", targetMeetingIndex)
                .toString();
    }
}
