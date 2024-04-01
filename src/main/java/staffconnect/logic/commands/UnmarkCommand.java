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
 * Unmarks a person as favourite in the staff book.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unmarks a person as favourite in the staff book. "
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNMARK_PERSON_SUCCESS = "Person unmarked as favourite: %1$s";

    public static final String MESSAGE_DUPLICATE_UNMARK = "This person has already been unmarked in the staff book.";

    private final Index targetIndex;

    /**
     * @param targetIndex of the person in the filtered person list to unmark.
     */
    public UnmarkCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToUnmark = lastShownList.get(targetIndex.getZeroBased());

        if (!personToUnmark.getFavourite().hasFavourite()) {
            throw new CommandException(MESSAGE_DUPLICATE_UNMARK);
        }

        Person unmarkedPerson = createUnmarkedPerson(personToUnmark);

        model.setPerson(personToUnmark, unmarkedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_UNMARK_PERSON_SUCCESS, Messages.format(unmarkedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToUnmark}.
     */
    private static Person createUnmarkedPerson(Person personToUnmark) {
        assert personToUnmark != null;

        Name name = personToUnmark.getName();
        Phone phone = personToUnmark.getPhone();
        Email email = personToUnmark.getEmail();
        Module module = personToUnmark.getModule();
        Faculty faculty = personToUnmark.getFaculty();
        Venue venue = personToUnmark.getVenue();
        Set<Tag> tags = personToUnmark.getTags();
        Set<Availability> availabilities = personToUnmark.getAvailabilities();
        Favourite updatedFavourite = new Favourite(false);

        Person unmarkedPerson =
                new Person(name, phone, email, module, faculty, venue, tags, availabilities, updatedFavourite);
        unmarkedPerson.setMeetings(personToUnmark.getMeetings());
        return unmarkedPerson;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnmarkCommand)) {
            return false;
        }

        UnmarkCommand otherUnmarkCommand = (UnmarkCommand) other;
        return targetIndex.equals(otherUnmarkCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
