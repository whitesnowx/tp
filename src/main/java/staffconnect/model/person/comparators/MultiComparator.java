package staffconnect.model.person.comparators;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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

    @Override
    public String toString() {
        StringBuilder sortStringBuilder = new StringBuilder();
        for (int i = 0; i < comparators.size(); i++) {
            Comparator<Person> comparator = comparators.get(i);
            sortStringBuilder.append(comparator.toString());
            if (i < comparators.size() - 1) {
                sortStringBuilder.append(", ");
            }
        }
        return sortStringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MultiComparator other = (MultiComparator) obj;
        return Objects.equals(comparators, other.comparators);
    }

}
