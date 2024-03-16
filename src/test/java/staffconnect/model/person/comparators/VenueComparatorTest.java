package staffconnect.model.person.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.model.person.comparators.FacultyComparator.FACULTY_COMPARATOR;
import static staffconnect.model.person.comparators.ModuleComparator.MODULE_COMPARATOR;
import static staffconnect.model.person.comparators.NameComparator.NAME_COMPARATOR;
import static staffconnect.model.person.comparators.PhoneComparator.PHONE_COMPARATOR;
import static staffconnect.model.person.comparators.VenueComparator.VENUE_COMPARATOR;
import static staffconnect.testutil.TypicalPersons.CARL;
import static staffconnect.testutil.TypicalPersons.ELLE;
import static staffconnect.testutil.TypicalPersons.GEORGE;

import org.junit.jupiter.api.Test;

public class VenueComparatorTest {
    @Test
    public void doesNotEquals() {
        assertNotEquals(VENUE_COMPARATOR, FACULTY_COMPARATOR);
        assertNotEquals(VENUE_COMPARATOR, MODULE_COMPARATOR);
        assertNotEquals(VENUE_COMPARATOR, NAME_COMPARATOR);
        assertNotEquals(VENUE_COMPARATOR, PHONE_COMPARATOR);
    }

    @Test
    public void checkComparator() {

        assertTrue(PHONE_COMPARATOR.compare(ELLE, CARL) <= -1); // michegan ave < wall street
        assertTrue(PHONE_COMPARATOR.compare(CARL, ELLE) >= 1); // wall street > michegan ave

        assertTrue(PHONE_COMPARATOR.compare(ELLE, GEORGE) <= -1); // michegan ave < 4th street
        assertTrue(PHONE_COMPARATOR.compare(GEORGE, ELLE) >= 1); //  4th street > michegan ave

        assertEquals(PHONE_COMPARATOR.compare(ELLE, ELLE), 0); // michegan ave = michegan ave
        assertEquals(PHONE_COMPARATOR.compare(GEORGE, GEORGE), 0); // 4th street = 4th street
    }
}
