package staffconnect.logic.commands;

import static java.util.Objects.requireNonNull;
import static staffconnect.logic.parser.CliSyntax.PREFIX_MEETING_DESCRIPTION;
import static staffconnect.logic.parser.CliSyntax.PREFIX_MEETING_STARTDATE;
import static staffconnect.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static staffconnect.model.meeting.comparator.MeetingDateThenDescriptionComparator.MEETING_DATE_THEN_DESCRIPTION_COMPARATOR;

import java.util.List;

import staffconnect.commons.core.index.Index;
import staffconnect.commons.util.ToStringBuilder;
import staffconnect.logic.Messages;
import staffconnect.logic.commands.exceptions.CommandException;
import staffconnect.model.Model;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.person.Person;
import staffconnect.model.person.PersonUtil;

/**
 * Adds a meeting to the staff book.
 */
public class AddMeetingCommand extends Command {

    public static final String COMMAND_WORD = "meeting-add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a meeting to the person identified "
            + "by the index number used in the displayed person list. \n"
            + "Parameters: INDEX (must be a positive integer) " + PREFIX_MEETING_DESCRIPTION + "DESCRIPTION "
            + PREFIX_MEETING_STARTDATE + "DATETIME[dd/MM/yyyy HH:mm] \n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_MEETING_DESCRIPTION
            + "Meet for finals " + PREFIX_MEETING_STARTDATE + "12/04/2023 18:00";


    public static final String MESSAGE_SUCCESS = "New meeting added: %1$s";
    public static final String MESSAGE_DUPLICATE_MEETING = "This meeting is already planned for this person!";

    private final Meeting toAdd;
    private final Index index;

    /**
     * Creates an AddMeetingCommand to add the specified {@code Meeting}
     */
    public AddMeetingCommand(Index index, Meeting meeting) {
        requireNonNull(index);
        requireNonNull(meeting);
        this.index = index;
        toAdd = meeting;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getSortedFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = PersonUtil.copyPerson(personToEdit);

        if (personToEdit.hasDuplicateMeeting(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_MEETING);
        }

        editedPerson.addMeetings(toAdd);
        editedPerson.updateSortedMeetingList(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR);

        //setPerson to force update the ui with the new items
        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddMeetingCommand)) {
            return false;
        }

        AddMeetingCommand otherAddMeetingCommand = (AddMeetingCommand) other;
        return index.equals(otherAddMeetingCommand.index) && toAdd.equals(otherAddMeetingCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("index", index).add("toAdd", toAdd).toString();
    }
}
