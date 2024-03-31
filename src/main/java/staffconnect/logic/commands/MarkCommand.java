package staffconnect.logic.commands;

import static java.util.Objects.requireNonNull;
import static staffconnect.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Set;

import staffconnect.commons.core.index.Index;
import staffconnect.commons.util.ToStringBuilder;
import staffconnect.logic.Messages;
import staffconnect.logic.commands.exceptions.CommandException;
import staffconnect.model.Model;
import staffconnect.model.availability.Availability;
import staffconnect.model.person.Email;
import staffconnect.model.person.Faculty;
import staffconnect.model.person.Favourite;
import staffconnect.model.person.Module;
import staffconnect.model.person.Name;
import staffconnect.model.person.Person;
import staffconnect.model.person.Phone;
import staffconnect.model.person.Venue;
import staffconnect.model.tag.Tag;

/**
 * Marks a person as favourite in the staff book.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks a person as favourite in the staff book. "
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MARK_PERSON_SUCCESS = "Person marked as favourite: %1$s";

    public static final String MESSAGE_DUPLICATE_MARK = "This person has already been marked in the staff book.";

    private final Index targetIndex;

    /**
     * @param targetIndex of the person in the filtered person list to mark.
     */
    public MarkCommand(Index targetIndex) {
        requireNonNull(targetIndex);

        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToMark = lastShownList.get(targetIndex.getZeroBased());

        if (personToMark.getFavourite().hasFavourite()) {
            throw new CommandException(MESSAGE_DUPLICATE_MARK);
        }

        Person markedPerson = createMarkedPerson(personToMark);

        model.setPerson(personToMark, markedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_MARK_PERSON_SUCCESS, Messages.format(markedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToMark}.
     */
    private static Person createMarkedPerson(Person personToMark) {
        assert personToMark != null;

        Name name = personToMark.getName();
        Phone phone = personToMark.getPhone();
        Email email = personToMark.getEmail();
        Module module = personToMark.getModule();
        Faculty faculty = personToMark.getFaculty();
        Venue venue = personToMark.getVenue();
        Set<Tag> tags = personToMark.getTags();
        Set<Availability> availabilities = personToMark.getAvailabilities();
        Favourite updatedFavourite = new Favourite(true);

        return new Person(name, phone, email, module, faculty, venue, tags, availabilities,
                updatedFavourite);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MarkCommand)) {
            return false;
        }

        MarkCommand otherMarkCommand = (MarkCommand) other;
        return targetIndex.equals(otherMarkCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
