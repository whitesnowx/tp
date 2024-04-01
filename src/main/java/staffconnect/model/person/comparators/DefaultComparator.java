package staffconnect.model.person.comparators;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import staffconnect.model.StaffBook;
import staffconnect.model.person.Person;

/**
 * Compares two contacts on the basis of the contact added earlier ordered
 * in front of the other contact.
 */
public class DefaultComparator implements Comparator<Person> {

    public static final DefaultComparator DEFAULT_COMPARATOR = new DefaultComparator();
    private StaffBook staffBook;

    /**
     * Constructs a {@code DefaultComparator} with the given staff book.
     *
     * @param staffBook A valid staff book.
     */
    public DefaultComparator(StaffBook staffBook) {
        requireNonNull(staffBook);
        this.staffBook = staffBook;
    }

    /**
     * Constructs a {@code DefaultComparator} with an empty staff book.
     */
    public DefaultComparator() {
        this.staffBook = new StaffBook();
    }

    @Override
    public int compare(Person p1, Person p2) {
        return Integer.compare(staffBook.indexOf(p1), staffBook.indexOf(p2));
    }
}
