package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_CHARLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SUBJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SUBJECT2;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CHARLES;
import static seedu.address.testutil.TypicalPersons.DUNCAN;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.ParentBuilder;
import seedu.address.testutil.StudentBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Student person = new StudentBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));
        assertTrue(CHARLES.isSamePerson(CHARLES));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));
        assertFalse(CHARLES.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new StudentBuilder(ALICE).withPhone(VALID_PHONE_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_SUBJECT).build();
        assertTrue(ALICE.isSamePerson(editedAlice));
        Person editedCharles = new ParentBuilder(CHARLES).withPhone(VALID_PHONE_BOB)
                .withAddress(VALID_ADDRESS_BOB).build();
        assertTrue(CHARLES.isSamePerson(editedCharles));

        // different name, all other attributes same -> returns false
        editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));
        editedCharles = new ParentBuilder(CHARLES).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedCharles));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new StudentBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));
        editedCharles = new ParentBuilder(CHARLES).withName(VALID_NAME_CHARLES.toLowerCase()).build();
        assertTrue(CHARLES.isSamePerson(editedCharles));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new StudentBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
        nameWithTrailingSpaces = VALID_NAME_CHARLES + " ";
        editedCharles = new ParentBuilder(CHARLES).withName(nameWithTrailingSpaces).build();
        assertFalse(CHARLES.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new StudentBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));
        Person charlesCopy = new ParentBuilder(CHARLES).build();
        assertTrue(CHARLES.equals(charlesCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));
        assertTrue(CHARLES.equals(CHARLES));

        // null -> returns false
        assertFalse(ALICE.equals(null));
        assertFalse(CHARLES.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));
        assertFalse(CHARLES.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB)); //Compare Student to Student
        assertFalse(ALICE.equals(DUNCAN)); //Compare Student to Parent
        assertFalse(CHARLES.equals(BOB)); //Compare Parent to Student
        assertFalse(CHARLES.equals(DUNCAN)); //Compare Parent to Parent

        // different name -> returns false
        Person editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));
        Person editedCharles = new ParentBuilder(CHARLES).withName(VALID_NAME_BOB).build();
        assertFalse(CHARLES.equals(editedCharles));

        // different phone -> returns false
        editedAlice = new StudentBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));
        editedCharles = new ParentBuilder(CHARLES).withPhone(VALID_PHONE_BOB).build();
        assertFalse(CHARLES.equals(editedCharles));

        // different address -> returns false
        editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));
        editedCharles = new ParentBuilder(CHARLES).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(CHARLES.equals(editedCharles));

        // different tags -> returns TRUE (previously false)
        editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_SUBJECT2).build();
        assertTrue(ALICE.equals(editedAlice));
    }

    @Test
    public void testEqualsAndHashCodeConsistency() {
        Person aliceCopy = new StudentBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));
        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());

        Person charlesCopy = new ParentBuilder(CHARLES).build();
        assertTrue(CHARLES.equals(charlesCopy));
        assertEquals(CHARLES.hashCode(), charlesCopy.hashCode());
    }

    @Test
    public void toStringMethod() {
        String expected = Student.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", address=" + ALICE.getAddress() + ", role=" + ALICE.getRole()
                + ", remark=" + ALICE.getRemark() + ", tags=" + ALICE.getTags()
                + ", parent=" + ALICE.getParentName() + "}";
        assertEquals(expected, ALICE.toString());

        expected = Parent.class.getCanonicalName() + "{name=" + CHARLES.getName() + ", phone=" + CHARLES.getPhone()
                + ", address=" + CHARLES.getAddress() + ", role=" + CHARLES.getRole()
                + ", remark=" + CHARLES.getRemark()
                + ", children=" + CHARLES.getChildrenNames() + "}";
        assertEquals(expected, CHARLES.toString());
    }
}
