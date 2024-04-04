package staffconnect.model.person.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.model.person.comparators.ModuleComparator.MODULE_COMPARATOR;
import static staffconnect.model.person.comparators.NameComparator.NAME_COMPARATOR;
import static staffconnect.testutil.TypicalPersons.ALICE;
import static staffconnect.testutil.TypicalPersons.CLARA;
import static staffconnect.testutil.TypicalPersons.GEORGE;

import java.util.List;

import org.junit.jupiter.api.Test;

public class MultiComparatorTest {

    @Test
    public void doesNotEquals() {

        MultiComparator sortModuleThenNameComparator = new MultiComparator(List.of(MODULE_COMPARATOR, NAME_COMPARATOR));

        //same comparator
        assertEquals(sortModuleThenNameComparator, sortModuleThenNameComparator);

        //correct order
        assertEquals(new MultiComparator(List.of(MODULE_COMPARATOR, NAME_COMPARATOR)), sortModuleThenNameComparator);

        //wrong order
        assertNotEquals(new MultiComparator(List.of(NAME_COMPARATOR, MODULE_COMPARATOR)), sortModuleThenNameComparator);

        //missing 1 attribute
        assertNotEquals(new MultiComparator(List.of(MODULE_COMPARATOR)), sortModuleThenNameComparator);

        //not null
        assertNotEquals(sortModuleThenNameComparator, null);
    }

    @Test
    public void checkComparator() {
        MultiComparator sortModuleThenNameComparator = new MultiComparator(List.of(MODULE_COMPARATOR, NAME_COMPARATOR));

        assertTrue(sortModuleThenNameComparator.compare(ALICE, CLARA) <= -1); // CS1101S < CS2102
        assertTrue(sortModuleThenNameComparator.compare(CLARA, ALICE) >= 1); //  CS2102 > CS1101S

        assertEquals(sortModuleThenNameComparator.compare(CLARA, CLARA), 0); // CS2102 = CS2102
        assertEquals(sortModuleThenNameComparator.compare(GEORGE, GEORGE), 0); // CS2102 = CS2102
        assertTrue(sortModuleThenNameComparator.compare(CLARA, GEORGE) <= -1); // CS2102 = CS2102, CLARA < GEORGE
    }

    @Test
    public void toStringTest() {
        // 1 attribute, correct attribute
        assertEquals(new MultiComparator(List.of(MODULE_COMPARATOR)).toString(), MODULE_COMPARATOR.toString());

        // 1 attribute, wrong attribute
        assertNotEquals(new MultiComparator(List.of(MODULE_COMPARATOR)).toString(), NAME_COMPARATOR.toString());

        //multiple correct attribute, correct order
        assertEquals(new MultiComparator(List.of(NAME_COMPARATOR, MODULE_COMPARATOR)).toString(),
                NAME_COMPARATOR + ", " + MODULE_COMPARATOR);

        //multiple correct attribute, wrong order
        assertNotEquals(new MultiComparator(List.of(NAME_COMPARATOR, MODULE_COMPARATOR)).toString(),
                MODULE_COMPARATOR + ", " + NAME_COMPARATOR);

        // not a multiComparator
        assertNotEquals(new MultiComparator(List.of(NAME_COMPARATOR, MODULE_COMPARATOR)).toString(), 5);
    }
}
