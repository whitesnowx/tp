package staffconnect.model.meeting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static staffconnect.logic.commands.CommandTestUtil.VALID_MEETING;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class MeetingDescriptionContainsKeywordPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        MeetingDescriptionContainsKeywordPredicate firstPredicate =
                new MeetingDescriptionContainsKeywordPredicate(firstPredicateKeywordList);
        MeetingDescriptionContainsKeywordPredicate secondPredicate =
                new MeetingDescriptionContainsKeywordPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertEquals(firstPredicate, firstPredicate);

        // same values -> returns true
        MeetingDescriptionContainsKeywordPredicate firstPredicateCopy =
                new MeetingDescriptionContainsKeywordPredicate(firstPredicateKeywordList);

        assertEquals(firstPredicate, firstPredicateCopy);

        // different types -> returns false
        assertNotEquals(1, firstPredicate);

        // null -> returns false
        assertNotEquals(null, firstPredicate);

        // different meeting description -> returns false
        assertNotEquals(firstPredicate, secondPredicate);
    }

    @Test
    public void test_descriptionContainsKeywords_returnsTrue() {

        // One keyword
        MeetingDescriptionContainsKeywordPredicate predicate =
                new MeetingDescriptionContainsKeywordPredicate(Collections.singletonList("Meet"));
        assertTrue(predicate.test(VALID_MEETING));

        // Multiple keywords
        predicate = new MeetingDescriptionContainsKeywordPredicate(Arrays.asList("Meet", "for"));
        assertTrue(predicate.test(VALID_MEETING));

        // Only one matching keyword
        predicate = new MeetingDescriptionContainsKeywordPredicate(Arrays.asList("Study", "for"));
        assertTrue(predicate.test(VALID_MEETING));

        // Mixed-case keywords
        predicate = new MeetingDescriptionContainsKeywordPredicate(Arrays.asList("mEeT", "fOR"));
        assertTrue(predicate.test(VALID_MEETING));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        MeetingDescriptionContainsKeywordPredicate predicate =
                new MeetingDescriptionContainsKeywordPredicate(Collections.emptyList());
        assertFalse(predicate.test(VALID_MEETING));

        // Non-matching keyword
        predicate = new MeetingDescriptionContainsKeywordPredicate(List.of("Carol"));
        assertFalse(predicate.test(VALID_MEETING));

        // Keywords match date but does not match description
        predicate = new MeetingDescriptionContainsKeywordPredicate(Arrays.asList("12/03/2023", "18:00"));
        assertFalse(predicate.test(VALID_MEETING));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        MeetingDescriptionContainsKeywordPredicate predicate = new MeetingDescriptionContainsKeywordPredicate(keywords);

        String expected = MeetingDescriptionContainsKeywordPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
