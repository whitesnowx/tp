package staffconnect.model.meeting;

import static java.util.Objects.requireNonNull;
import static staffconnect.commons.util.CollectionUtil.requireAllNonNull;
import static staffconnect.model.meeting.comparator.MeetingDateThenDescriptionComparator.MEETING_DATE_THEN_DESCRIPTION_COMPARATOR;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

/**
 * Represents the in-memory model of the person's meetings
 */
public class MeetingManager {

    public static final Predicate<Meeting> PREDICATE_SHOW_ALL_MEETINGS = unused -> true;

    private final MeetingBook meetingBook;
    private final FilteredList<Meeting> filteredMeetings;
    private final SortedList<Meeting> sortedFilteredMeetings;

    public MeetingManager() {
        this(new MeetingBook());
    }

    /**
     * Initializes a MeetingBook with the given {@code meetingBook}.
     */
    public MeetingManager(ReadOnlyMeetingBook meetingBook) {

        requireAllNonNull(meetingBook);
        this.meetingBook = new MeetingBook(meetingBook);
        filteredMeetings = new FilteredList<>(this.meetingBook.getMeetingList());
        sortedFilteredMeetings = new SortedList<>(filteredMeetings);
        updateSortedMeetingList(MEETING_DATE_THEN_DESCRIPTION_COMPARATOR); //sets the default view to be sorted by date.
    }


    //=========== UniqueMeetingList ================================================================================

    /**
     * Updates the sort attribute of the sorted meeting list of MeetingBook to sort by the given comparator.
     *
     * @param comparator to decide how to sort the meetings.
     */
    public void updateSortedMeetingList(Comparator<Meeting> comparator) {
        requireNonNull(comparator);
        filteredMeetings.setPredicate(null);
        sortedFilteredMeetings.setComparator(comparator);
    }

    /**
     * Returns the MeetingBook.
     *
     */
    public ReadOnlyMeetingBook getMeetingBook() {
        return meetingBook;
    }

    /**
     * Replaces all the current meeting data in MeetingBook with the current list.
     *
     * @param meetingBook the input meeting list to replace to.
     */
    public void setMeetingBook(List<Meeting> meetingBook) {
        this.meetingBook.setMeetings(meetingBook);
    }

    /**
     * Checks if the current MeetingBook contains the same meeting.
     *
     * @param meeting the meeting to check.
     * @return true if a meeting with the same identity as {@code meeting} exists in the meeting list.
     */
    public boolean hasMeeting(Meeting meeting) {
        requireNonNull(meeting);
        return meetingBook.hasMeeting(meeting);
    }

    /**
     * Removes the specified meeting from the MeetingBook.
     *
     * @param target meeting to remove .
     */
    public void deleteMeeting(Meeting target) {
        meetingBook.removeMeeting(target);
    }

    /**
     * Adds a meeting to the current list within MeetingBook.
     *
     * @param meeting to add to the MeetingBook.
     */
    public void addMeeting(Meeting meeting) {
        meetingBook.addMeeting(meeting);
        updateFilteredMeetingList(PREDICATE_SHOW_ALL_MEETINGS);
    }


    //=========== Filtered Meeting List Accessors =============================================================

    /**
     * Updates the filter of the filtered meeting list of MeetingBook to filter by the given {@code predicate}.
     *
     * @param predicate to filter the list to.
     */
    public void updateFilteredMeetingList(Predicate<Meeting> predicate) {
        requireNonNull(predicate);
        filteredMeetings.setPredicate(predicate);
        sortedFilteredMeetings.setComparator(null);
    }

    /**
     * Gets an unmodifiable list of meetings.
     *
     * @return an ObservableList of meetings.
     */
    public ObservableList<Meeting> getMeetingList() {
        return meetingBook.getMeetingList();
    }

    /**
     * Returns an unmodifiable view of the list of {@code meeting}.
     */
    public ObservableList<Meeting> getFilteredMeetingList() {
        return sortedFilteredMeetings;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MeetingManager)) {
            return false;
        }

        MeetingManager otherModelManager = (MeetingManager) other;
        return meetingBook.equals(otherModelManager.meetingBook)
                && filteredMeetings.equals(otherModelManager.filteredMeetings);
    }

}
