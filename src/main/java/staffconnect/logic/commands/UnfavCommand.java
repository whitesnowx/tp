package staffconnect.logic.commands;

import static java.util.Objects.requireNonNull;
import static staffconnect.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static staffconnect.model.person.PersonUtil.createPersonWithFavouriteStatus;

import java.util.List;

import staffconnect.commons.core.index.Index;
import staffconnect.commons.util.ToStringBuilder;
import staffconnect.logic.Messages;
import staffconnect.logic.commands.exceptions.CommandException;
import staffconnect.model.Model;
import staffconnect.model.person.Favourite;
import staffconnect.model.person.Person;

/**
 * Removes a person as favourite in the staff book.
 */
public class UnfavCommand extends Command {

    public static final String COMMAND_WORD = "unfav";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes a person as favourite in the staff book. "
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_REMOVE_PERSON_FAVOURITE_SUCCESS = "Person removed as favourite: %1$s";

    public static final String MESSAGE_DUPLICATE_UNFAV = "This person is already no longer your favourite "
            + "in the staff book.";

    private final Index targetIndex;

    /**
     * @param targetIndex of the person in the filtered person list to remove as favourite.
     */
    public UnfavCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToUnfav = lastShownList.get(targetIndex.getZeroBased());

        if (!personToUnfav.getFavourite().hasFavourite()) {
            throw new CommandException(MESSAGE_DUPLICATE_UNFAV);
        }

        Person unfavPerson = createUnfavPerson(personToUnfav);

        model.setPerson(personToUnfav, unfavPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_REMOVE_PERSON_FAVOURITE_SUCCESS, Messages.format(unfavPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToUnfav}.
     */
    private static Person createUnfavPerson(Person personToUnfav) {
        assert personToUnfav != null;

        Favourite updatedFavourite = new Favourite(false);
        Person unfavPerson = createPersonWithFavouriteStatus(personToUnfav, updatedFavourite);
        return unfavPerson;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnfavCommand)) {
            return false;
        }

        UnfavCommand otherUnfavCommand = (UnfavCommand) other;
        return targetIndex.equals(otherUnfavCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
