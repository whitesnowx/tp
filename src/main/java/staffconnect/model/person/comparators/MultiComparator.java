package staffconnect.model.person.comparators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import staffconnect.model.person.Person;

/**
 * Represents a composite comparator that combines multiple comparators into one.
 */
public class MultiComparator implements Comparator<Person> {
    private final List<Comparator<Person>> comparators;

    public MultiComparator(List<Comparator<Person>> comparators) {
        this.comparators = comparators;
    }

    @Override
    public int compare(Person person1, Person person2) {
        for (Comparator<Person> comparator : comparators) {
            int result = comparator.compare(person1, person2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
