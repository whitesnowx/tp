package staffconnect.model.person;

import java.util.HashSet;
import java.util.List;

import staffconnect.model.availability.Availability;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.tag.Tag;


/**
 * Contains utility methods used for dealing with data in the various *Person classes.
 */
public class PersonUtil {

    /**
     * Copies and returns a new Person with the data of {@code personToCopy}.
     */
    public static Person copyPersonWithMeetings(Person personToCopy) {
        Name name = personToCopy.getName();
        Phone phone = personToCopy.getPhone();
        Email email = personToCopy.getEmail();
        Module module = personToCopy.getModule();
        Faculty faculty = personToCopy.getFaculty();
        Venue venue = personToCopy.getVenue();
        HashSet<Tag> tags = new HashSet<>(personToCopy.getTags());
        HashSet<Availability> availabilities = new HashSet<>(personToCopy.getAvailabilities());
        List<Meeting> meetings = personToCopy.getMeetings();

        Person personToCreate = new Person(name, phone, email, module, faculty, venue, tags, availabilities);
        personToCreate.setMeetings(meetings);
        return personToCreate;
    }
}
