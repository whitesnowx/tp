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
 * Sets a person as favourite in the staff book.
 */
public class FavCommand extends Command {

    public static final String COMMAND_WORD = "fav";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets a person as favourite in the staff book. "
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SET_PERSON_FAVOURITE_SUCCESS = "Person set as favourite: %1$s";

    public static final String MESSAGE_DUPLICATE_FAV = "This person is already your favourite in the staff book.";

    private final Index targetIndex;

    /**
     * @param targetIndex of the person in the filtered person list to set as favourite.
     */
    public FavCommand(Index targetIndex) {
        requireNonNull(targetIndex);

        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToFav = lastShownList.get(targetIndex.getZeroBased());

        if (personToFav.getFavourite().hasFavourite()) {
            throw new CommandException(MESSAGE_DUPLICATE_FAV);
        }

        Person favPerson = createFavPerson(personToFav);

        model.setPerson(personToFav, favPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SET_PERSON_FAVOURITE_SUCCESS, Messages.format(favPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToFav}.
     */
    private static Person createFavPerson(Person personToFav) {
        assert personToFav != null;

        Name name = personToFav.getName();
        Phone phone = personToFav.getPhone();
        Email email = personToFav.getEmail();
        Module module = personToFav.getModule();
        Faculty faculty = personToFav.getFaculty();
        Venue venue = personToFav.getVenue();
        Set<Tag> tags = personToFav.getTags();
        Set<Availability> availabilities = personToFav.getAvailabilities();
        Favourite updatedFavourite = new Favourite(true);

        Person favPerson =
                new Person(name, phone, email, module, faculty, venue, tags, availabilities, updatedFavourite);
        favPerson.setMeetings(personToFav.getMeetings());
        return favPerson;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FavCommand)) {
            return false;
        }

        FavCommand otherFavCommand = (FavCommand) other;
        return targetIndex.equals(otherFavCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
