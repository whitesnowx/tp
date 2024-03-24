package staffconnect.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;


class DescriptionTest {


    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new MeetingDescription(null));
    }

    @Test
    public void constructor_invalidDescription_throwsIllegalArgumentException() {
        String invalidDescription = "";
        assertThrows(IllegalArgumentException.class, () -> new MeetingDescription(invalidDescription));
    }

    @Test
    public void isValidDescription() {
        // null MeetingDescription
        assertThrows(NullPointerException.class, () -> MeetingDescription.isValidDescription(null));

        // invalid MeetingDescription
        assertFalse(MeetingDescription.isValidDescription("")); // empty string
        assertFalse(MeetingDescription.isValidDescription("   ")); // spaces only
        assertFalse(MeetingDescription.isValidDescription("^")); // only non-alphanumeric characters
        assertFalse(MeetingDescription.isValidDescription("peter meeting*")); // contains non-alphanumeric characters

        // valid MeetingDescription
        assertTrue(MeetingDescription.isValidDescription("meeting")); // alphabets only
        assertTrue(MeetingDescription.isValidDescription("12345")); // numbers only
        assertTrue(MeetingDescription.isValidDescription("meeting at for 2nd finals")); // alphanumeric characters
        assertTrue(MeetingDescription.isValidDescription("Crush the exam")); // with capital letters
        assertTrue(MeetingDescription.isValidDescription("Super hard midterm with finals and project combined 2nd"));
    }

    @Test
    public void equals() {
        MeetingDescription description = new MeetingDescription("Valid MeetingDescription");
        MeetDateTime testDate = new MeetDateTime("12/04/2023 12:00");

        // same values -> returns true
        assertEquals(description, new MeetingDescription("Valid MeetingDescription"));

        // same object -> returns true
        assertEquals(description, description);

        // null -> returns false
        assertNotEquals(null, description);

        // different types -> returns false
        assertNotEquals(5.0f, description);

        //Different object type -> returns false
        assertFalse(description.equals(testDate));

        // different values -> returns false
        assertNotEquals(description, new MeetingDescription("Other valid description"));
    }
    @Test
    public void asSymmetricHashcode() {
        MeetingDescription first = new MeetingDescription("Valid MeetingDescription");
        MeetingDescription second = new MeetingDescription("Valid MeetingDescription");
        assertEquals(first.hashCode(), second.hashCode());
    }
}
