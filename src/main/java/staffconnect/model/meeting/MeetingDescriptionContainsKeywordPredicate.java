package staffconnect.model.meeting;

import java.util.List;
import java.util.function.Predicate;

import staffconnect.commons.util.StringUtil;
import staffconnect.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Meeting}'s {@code MeetingDescription} matches any of the keywords given.
 */
public class MeetingDescriptionContainsKeywordPredicate implements Predicate<Meeting> {
    private final List<String> keywords;

    public MeetingDescriptionContainsKeywordPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Meeting meeting) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(meeting.getDescription().description, keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MeetingDescriptionContainsKeywordPredicate)) {
            return false;
        }

        MeetingDescriptionContainsKeywordPredicate
                otherNameContainsKeywordsPredicate = (MeetingDescriptionContainsKeywordPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
