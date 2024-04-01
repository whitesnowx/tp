package staffconnect.model.person.comparators;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import staffconnect.model.person.Person;

/**
 * Compares two contacts on the basis of the favourite status of the contact,
 * if equal, it compares the contacts with the given comparator.
 */
public class FavouriteComparator implements Comparator<Person> {

    private Comparator<Person> comparator;
    /**
     * Constructs a {@code FavouriteComparator} with the given comparator.
     */
    public FavouriteComparator(Comparator<Person> comparator) {
        requireNonNull(comparator);
        this.comparator = comparator;
    }

    @Override
    public int compare(Person p1, Person p2) {
        if (p1.getFavourite().hasFavourite() && !p2.getFavourite().hasFavourite()) {
            return -1;
        } else if (!p1.getFavourite().hasFavourite() && p2.getFavourite().hasFavourite()) {
            return 1;
        }

        return comparator.compare(p1, p2);
    }
}
