package seedu.address.model.person;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SUBJECT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CHARLES;
import static seedu.address.testutil.TypicalPersons.IVAN;
import static seedu.address.testutil.TypicalPersons.KELLY;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.StudentBuilder;

public class UniquePersonListTest {

    private final UniquePersonList uniquePersonList = new UniquePersonList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.contains(null));
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(uniquePersonList.contains(ALICE));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        uniquePersonList.add(ALICE);
        assertTrue(uniquePersonList.contains(ALICE));
    }

    @Test
    public void contains_personWithSameIdentityFieldsInList_returnsTrue() {
        uniquePersonList.add(ALICE);
        Person editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_SUBJECT)
                .build();
        assertTrue(uniquePersonList.contains(editedAlice));
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.add(null));
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePersonException() {
        uniquePersonList.add(ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniquePersonList.add(ALICE));
    }

    @Test
    public void setPerson_nullTargetPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPerson(null, ALICE));
    }

    @Test
    public void setPerson_nullEditedPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPerson(ALICE, null));
    }

    @Test
    public void setPerson_targetPersonNotInList_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniquePersonList.setPerson(ALICE, ALICE));
    }

    @Test
    public void setPerson_editedPersonIsSamePerson_success() {
        uniquePersonList.add(ALICE);
        uniquePersonList.setPerson(ALICE, ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(ALICE);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasSameIdentity_success() {
        uniquePersonList.add(ALICE);
        Person editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_SUBJECT)
                .build();
        uniquePersonList.setPerson(ALICE, editedAlice);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(editedAlice);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasDifferentIdentity_success() {
        uniquePersonList.add(ALICE);
        uniquePersonList.setPerson(ALICE, BOB);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasNonUniqueIdentity_throwsDuplicatePersonException() {
        uniquePersonList.add(ALICE);
        uniquePersonList.add(BOB);
        assertThrows(DuplicatePersonException.class, () -> uniquePersonList.setPerson(ALICE, BOB));
    }

    @Test
    public void resolveParentLink_studentAndExistingParent_parentChildrenUpdated() {
        Student testStudent = new StudentBuilder(IVAN).build();
        uniquePersonList.add(CHARLES);
        uniquePersonList.add(testStudent);
        uniquePersonList.resolveParentLink(testStudent);
        assertTrue(CHARLES.hasChild(testStudent));
        assertTrue(CHARLES.hasChildName(testStudent.getName()));
    }

    @Test
    public void resolveParentLink_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.resolveParentLink(null));
    }

    @Test
    public void resolveParentLink_studentAndWithNoParent_nothingHappens() {
        uniquePersonList.add(CHARLES);
        uniquePersonList.resolveParentLink(ALICE);
        assertFalse(CHARLES.hasChild(ALICE));
        assertFalse(CHARLES.hasChildName(ALICE.getName()));
    }

    @Test
    public void destroyParentLink_existingStudentAndExistingParent_parentChildrenRemoved() {
        Student testStudent = new StudentBuilder(IVAN).build();
        uniquePersonList.add(CHARLES);
        uniquePersonList.add(testStudent);
        uniquePersonList.resolveParentLink(testStudent);
        assertTrue(CHARLES.hasChild(testStudent));
        assertTrue(CHARLES.hasChildName(testStudent.getName()));

        uniquePersonList.destroyParentLink(testStudent);
        assertFalse(CHARLES.hasChild(testStudent));
        assertFalse(CHARLES.hasChildName(testStudent.getName()));
        assertNull(testStudent.getParentName());
        assertNull(testStudent.getParent());
    }

    @Test
    public void destroyParentLink_studentAndWithNoParent_nothingHappens() {
        Student testStudent = new StudentBuilder(IVAN).build();
        uniquePersonList.add(CHARLES);
        uniquePersonList.resolveParentLink(testStudent);
        assertTrue(CHARLES.hasChild(testStudent));
        assertTrue(CHARLES.hasChildName(testStudent.getName()));

        uniquePersonList.destroyParentLink(ALICE);
        assertTrue(CHARLES.hasChild(testStudent));
        assertTrue(CHARLES.hasChildName(testStudent.getName()));
        assertNull(ALICE.getParentName());
        assertNull(ALICE.getParent());

        uniquePersonList.destroyParentLink(testStudent);
        assertFalse(CHARLES.hasChild(testStudent));
        assertFalse(CHARLES.hasChildName(testStudent.getName()));
        assertNull(testStudent.getParentName());
        assertNull(testStudent.getParent());
    }

    @Test
    public void destroyParentLink_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.destroyParentLink(null));
    }

    @Test
    public void resolveAllParentLinks_existingStudentAndExistingParent_allStudentsAndParentsUpdated() {
        Student testStudent = new StudentBuilder(IVAN).build();
        Student testStudent2 = new StudentBuilder(KELLY).build();
        uniquePersonList.add(CHARLES);
        uniquePersonList.add(testStudent);
        uniquePersonList.add(testStudent2);
        uniquePersonList.resolveAllParentLinks();
        assertTrue(CHARLES.hasChild(testStudent));
        assertTrue(CHARLES.hasChildName(testStudent.getName()));
        assertTrue(CHARLES.hasChild(testStudent2));
        assertTrue(CHARLES.hasChildName(testStudent2.getName()));
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.remove(null));
    }

    @Test
    public void remove_personDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniquePersonList.remove(ALICE));
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        uniquePersonList.add(ALICE);
        uniquePersonList.remove(ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_nullUniquePersonList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPersons((UniquePersonList) null));
    }

    @Test
    public void setPersons_uniquePersonList_replacesOwnListWithProvidedUniquePersonList() {
        uniquePersonList.add(ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        uniquePersonList.setPersons(expectedUniquePersonList);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPersons((List<Person>) null));
    }

    @Test
    public void setPersons_list_replacesOwnListWithProvidedList() {
        uniquePersonList.add(ALICE);
        List<Person> personList = Collections.singletonList(BOB);
        uniquePersonList.setPersons(personList);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_listWithDuplicatePersons_throwsDuplicatePersonException() {
        List<Person> listWithDuplicatePersons = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniquePersonList.setPersons(listWithDuplicatePersons));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniquePersonList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniquePersonList.asUnmodifiableObservableList().toString(), uniquePersonList.toString());
    }
}
