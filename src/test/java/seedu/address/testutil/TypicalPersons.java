package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Day;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Session;
import seedu.address.model.person.Student;
import seedu.address.model.person.Time;

import static seedu.address.logic.commands.CommandTestUtil.*;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Student ALICE = new StudentBuilder()
            .withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withPhone("94351253")
            .withRemark("She likes Math.")
            .withTags("math")
            .withSessions(new Session(new Day("Mon"), new Time("12pm-3pm"))).build();
    public static final Student BENSON = new StudentBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withPhone("98765432")
            .withRemark("He likes alcohol.")
            .withTags("science")
            .withSessions(new Session(new Day("Mon"), new Time("12pm-3pm"))).build();
    public static final Student CARL = new StudentBuilder().withName("Carl Kurz")
            .withPhone("95352563")
            .withAddress("wall street, Singapore 678901")
            .withSessions(new Session(new Day("Mon"), new Time("12pm-3pm"))).build();
    public static final Student DANIEL = new StudentBuilder().withName("Daniel Meier")
            .withPhone("87652533")
            .withAddress("10th street, Singapore 654321")
            .withTags("math")
            .withSessions(new Session(new Day("Mon"), new Time("12pm-3pm"))).build();
    public static final Student ELLE = new StudentBuilder().withName("Elle Meyer")
            .withPhone("94822245")
            .withAddress("michegan ave, City Hall, Singapore")
            .withSessions(new Session(new Day("Mon"), new Time("12pm-3pm"))).build();
    public static final Parent FIONA = new ParentBuilder()
            .withName("Fiona Kunz")
            .withPhone("94824276")
            .withAddress("little tokyo, Tanjong Pagar, Singapore").build();
    public static final Parent GEORGE = new ParentBuilder().withName("George Best")
            .withAddress("4th street, Fifth Avenue, Singapore")
            .withPhone("98765432")
            .withRemark("He likes to drink alcohol.").build();

    // Manually added
    public static final Student HOON = new StudentBuilder().withName("Hoon Meier")
            .withPhone("84824243")
            .withAddress("little india, Downtown Line, SMRT, Singapore")
            .withSessions(new Session(new Day("Mon"), new Time("12pm-3pm"))).build();
    public static final Parent IDA = new ParentBuilder().withName("Ida Mueller")
            .withPhone("84821310")
            .withAddress("chicago ave, East West Line, SBS, Singapore").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Student AMY = new StudentBuilder()
            .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withAddress(VALID_ADDRESS_AMY)
            .withRemark((VALID_REMARK_AMY))
            .withTags(VALID_TAG_SUBJECT)
            .withSessions(VALID_SESSION).build();
    public static final Student BOB = new StudentBuilder()
            .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withAddress(VALID_ADDRESS_BOB)
            .withRemark(VALID_REMARK_BOB)
            .withTags(VALID_TAG_SUBJECT, VALID_TAG_SUBJECT2)
            .withSessions(VALID_SESSION).build();
    public static final Parent CHARLES = new ParentBuilder()
            .withName(VALID_NAME_CHARLES)
            .withPhone(VALID_PHONE_CHARLES)
            .withAddress(VALID_ADDRESS_CHARLES)
            .withRemark(VALID_REMARK_CHARLES).build();
    public static final Parent DUNCAN = new ParentBuilder()
            .withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB)
            .withAddress(VALID_ADDRESS_BOB)
            .withRemark(VALID_REMARK_BOB).build();
    public static final Student JAMES = new StudentBuilder()
            .withName(VALID_NAME_JAMES)
            .withPhone(VALID_PHONE_BOB)
            .withAddress(VALID_ADDRESS_BOB)
            .withRemark(VALID_REMARK_BOB)
            .withTags(VALID_TAG_SUBJECT, VALID_TAG_SUBJECT2)
            .withParentName(VALID_NAME_CHARLES)
            .build();
    public static final Student IVAN = new StudentBuilder()
            .withName(VALID_NAME_IVAN)
            .withPhone(VALID_PHONE_BOB)
            .withAddress(VALID_ADDRESS_BOB)
            .withRemark(VALID_REMARK_BOB)
            .withTags(VALID_TAG_SUBJECT, VALID_TAG_SUBJECT2)
            .withParentName(VALID_NAME_CHARLES)
            .build();
    public static final Student KELLY = new StudentBuilder()
            .withName(VALID_NAME_KELLY)
            .withPhone(VALID_PHONE_BOB)
            .withAddress(VALID_ADDRESS_BOB)
            .withRemark(VALID_REMARK_BOB)
            .withTags(VALID_TAG_SUBJECT, VALID_TAG_SUBJECT2)
            .withParentName(VALID_NAME_CHARLES)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
