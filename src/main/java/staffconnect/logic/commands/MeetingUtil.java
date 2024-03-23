package staffconnect.logic.commands;

import java.util.HashSet;
import java.util.Set;

import staffconnect.model.availability.Availability;
import staffconnect.model.meeting.Meeting;
import staffconnect.model.person.Email;
import staffconnect.model.person.Faculty;
import staffconnect.model.person.Module;
import staffconnect.model.person.Name;
import staffconnect.model.person.Person;
import staffconnect.model.person.Phone;
import staffconnect.model.person.Venue;
import staffconnect.model.tag.Tag;

/**
 * Contains utility methods used for managing meetings in the various *MeetingCommand classes.
 */
public class MeetingUtil {

    /**
     * Adds a meeting to the current person.
     * @param personToEdit person to add the meeting to
     * @param meeting a valid meeting
     * @return a person with the meeting added
     */
    public static Person addMeetingToPerson(Person personToEdit, Meeting meeting) {
        assert personToEdit != null;

        Name currentName = personToEdit.getName();
        Phone currentPhone = personToEdit.getPhone();
        Email currentEmail = personToEdit.getEmail();
        Module currentModule = personToEdit.getModule();
        Faculty currentFaculty = personToEdit.getFaculty();
        Venue currentVenue = personToEdit.getVenue();
        Set<Tag> currentTags = personToEdit.getTags();
        Set<Availability> currentAvailability = personToEdit.getAvailabilities();
        Set<Meeting> currentMeetings = new HashSet<>(personToEdit.getMeetings()); //to reduce coupling with Person
        Person editedPerson =
                new Person(currentName, currentPhone, currentEmail, currentModule, currentFaculty, currentVenue,
                        currentTags,
                        currentAvailability);
        currentMeetings.add(meeting);
        editedPerson.setMeetings(currentMeetings);
        return editedPerson;
    }

}
