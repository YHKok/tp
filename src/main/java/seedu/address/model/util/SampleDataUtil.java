package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Day;
import seedu.address.model.person.Name;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Session;
import seedu.address.model.person.Student;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static final Remark EMPTY_REMARK = new Remark("");

    public static Person[] getSamplePersons() {
        return new Person[] {
            new Student(new Name("Alex Yeoh"), new Phone("87438807"),
                new Address("Blk 30 Geylang Street 29, #06-40"), new Remark("this is a remark"),
                getTagSet("math"), getSessionSet(new Session(new Day("Mon"), new Time("2pm-4pm")))),
            new Student(new Name("Bernice Yu"), new Phone("99272758"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), EMPTY_REMARK,
                getTagSet("science"), getSessionSet(new Session(new Day("Mon"), new Time("2pm-4pm")))),
            new Student(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), EMPTY_REMARK,
                getTagSet("english"), getSessionSet(new Session(new Day("Mon"), new Time("2pm-4pm")))),
            new Student(new Name("David Chew"), new Phone("91031282"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), EMPTY_REMARK,
                getTagSet("chinese"), getSessionSet(new Session(new Day("Mon"), new Time("2pm-4pm")))),
            new Student(new Name("Irfan Ibrahim"), new Phone("92492021"),
                new Address("Blk 47 Tampines Street 20, #17-35"), EMPTY_REMARK,
                getTagSet("math"), getSessionSet(new Session(new Day("Mon"), new Time("2pm-4pm")))),
            new Student(new Name("Roy Balakrishnan"), new Phone("92624417"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), EMPTY_REMARK,
                getTagSet("math"), getSessionSet(new Session(new Day("Mon"), new Time("2pm-4pm")))),
            new Parent(new Name("Robin Banks"), new Phone("96767679"),
                    new Address("Blk 67 My Grandfathers Road 67, #67-67"), EMPTY_REMARK)
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a session set containing the list of strings given.
     */
    public static Set<Session> getSessionSet(Session... session) {
        return Arrays.stream(session)
                .map(s -> new Session(new Day(s.getDay().getValue()), new Time(s.getTime().getValue())))
                .collect(Collectors.toSet());
    }

}
