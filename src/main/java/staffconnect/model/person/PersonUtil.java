package staffconnect.model.person;

import java.util.List;
import java.util.Set;

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
    public static Person copyPerson(Person personToCopy) {
        Name name = personToCopy.getName();
        Phone phone = personToCopy.getPhone();
        Email email = personToCopy.getEmail();
        Module module = personToCopy.getModule();
        Faculty faculty = personToCopy.getFaculty();
        Venue venue = personToCopy.getVenue();
        Set<Tag> tags = personToCopy.getTags();
        Set<Availability> availabilities = personToCopy.getAvailabilities();
        List<Meeting> meetings = personToCopy.getMeetings();
        Favourite favourite = personToCopy.getFavourite();

        Person personToCreate = new Person(name, phone, email, module, faculty, venue, tags, availabilities, favourite);
        personToCreate.setMeetings(meetings);
        return personToCreate;
    }
}
