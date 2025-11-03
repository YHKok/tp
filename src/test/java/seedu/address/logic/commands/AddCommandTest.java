package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
import seedu.address.testutil.StudentBuilder;

/**
 * Unit tests for {@link AddCommand}.
 * Focus: constructor guards, duplicate handling, parent linking constraint, equals & toString.
 */
public class AddCommandTest {

    /**
     * Ensure constructor rejects null person input.
     */
    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    /**
     * Happy path: model accepts add; feedback matches; state holds added person.
     */
    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new StudentBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    /**
     * Adding a duplicate person must fail with {@link CommandException}.
     */
    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new StudentBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PERSON, () -> addCommand.execute(modelStub));
    }

    /**
     * Adding a student whose parent link cannot be resolved must fail.
     */
    @Test
    public void execute_invalidParent_throwsCommandException() {
        Person validPerson = new StudentBuilder().build();
        Person validPerson2 = new StudentBuilder().withName("BOB").withParentName("Tom").build();
        AddCommand addCommand = new AddCommand(validPerson2);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddCommand.MESSAGE_INVALID_PARENT, () -> addCommand.execute(modelStub));
    }

    /**
     * Equality contract must hold across representative cases.
     */
    @Test
    public void equals() {
        Person alice = new StudentBuilder().withName("Alice").build();
        Person bob = new StudentBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> false
        assertFalse(addAliceCommand.equals(1));

        // null -> false
        assertFalse(addAliceCommand.equals(null));

        // different person -> false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * toString must include class name and person payload.
     */
    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * Default Model stub where every call fails.
     * Extend in tests to override required bits only.
     */
    private class ModelStub implements Model {
        /** {@inheritDoc} */
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public void linkParent(Student student) {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public void destroyStudentLinks(Parent parent) {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public void destroyParentLink(Student student) {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        /** {@inheritDoc} */
        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        /**
         * Sort hook used by new view-session flow.
         * No-op in this stub since add does not depend on order.
         * @param comparator person comparator
         */
        @Override
        public void sortFilteredPersonList(Comparator<Person> comparator) {
            // no-op
        }
    }

    /**
     * Model stub with single existing person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        /**
         * Build stub around given person.
         * @param person existing entry; not null
         */
        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * Model stub that records added persons and accepts all adds.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        /** {@inheritDoc} */
        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        /** {@inheritDoc} */
        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        /** {@inheritDoc} */
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
