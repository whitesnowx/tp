package staffconnect.model.person;

import static staffconnect.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import staffconnect.commons.util.ToStringBuilder;
import staffconnect.model.availability.Availability;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.meeting.MeetingManager;
import staffconnect.model.tag.Tag;

/**
 * Represents a Person in the staff book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Module module;
    private final Faculty faculty;
    private final Venue venue;
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Availability> availabilities = new HashSet<>();
    private final MeetingManager meetings = new MeetingManager();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Module module, Faculty faculty, Venue venue,
            Set<Tag> tags, Set<Availability> availabilities) {
        requireAllNonNull(name, phone, email, module, faculty, venue, tags, availabilities);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.module = module;
        this.faculty = faculty;
        this.venue = venue;
        this.tags.addAll(tags);
        this.availabilities.addAll(availabilities);
    }
    public Name getName() {
        return name;
    }
    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Module getModule() {
        return module;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public Venue getVenue() {
        return venue;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable availability set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Availability> getAvailabilities() {
        return Collections.unmodifiableSet(availabilities);
    }

    /**
     * Returns an immutable meeting set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public ObservableList<Meeting> getMeetings() {
        return meetings.getMeetingList();
    }

    /** Returns an unmodifiable view of the filtered meeting list. */
    public ObservableList<Meeting> getFilteredMeetings() {
        return meetings.getFilteredMeetingList();
    }

    /**
     * Updates the filter of the filtered meeting list to filter by the given {@code predicate}.
     * @param predicate to filter the list to.
     */
    public void updateFilteredMeetingList(Predicate<Meeting> predicate) {
        meetings.updateFilteredMeetingList(predicate);
    }

    /**
     * Updates the sort attribute of the sorted meeting list to sort by the given comparator.
     * @param comparator to decide how to sort the meetings.
     */
    public void updateSortedMeetingList(Comparator<Meeting> comparator) {
        meetings.updateSortedMeetingList(comparator);
    }

    /**
     * Replaces all the current meeting data with the current list.
     * @param toAdd the input meeting list to replace to.
     */
    public void setMeetings(List<Meeting> toAdd) {
        meetings.setMeetingBook(toAdd);
    }

    public void addMeetings(Meeting toAdd) {
        meetings.addMeeting(toAdd);
    }

    /**
     * Removes the specified meeting form the current meeting list.
     * @param toRemove the meeting.
     */

    public void removeMeeting(Meeting toRemove) {
        meetings.deleteMeeting(toRemove);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if the meeting to add is already tagged to the current person.
     */
    public boolean hasDuplicateMeeting(Meeting toAdd) {
        return meetings.hasMeeting(toAdd);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && module.equals(otherPerson.module)
                && faculty.equals(otherPerson.faculty)
                && venue.equals(otherPerson.venue)
                && tags.equals(otherPerson.tags)
                && availabilities.equals(otherPerson.availabilities);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, module, faculty, venue, tags, availabilities);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("module", module)
                .add("faculty", faculty)
                .add("venue", venue)
                .add("tags", tags)
                .add("availabilities", availabilities)
                .add("meetings", meetings.getMeetingList())
                .toString();
    }

}
