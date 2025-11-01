package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.nio.file.Path;
import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Day;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Session;
import seedu.address.model.person.Student;
import seedu.address.model.person.Time;
import seedu.address.model.session.SessionSlot;

/**
 * Integration tests for {@link ViewSessionCommand} using a minimal {@link Model} stub.
 * Goal: verify day-only filter plus earliest-start ordering for students with sessions.
 */
public class ViewSessionCommandExecuteTest {

    /**
     * Verify Monday filter reduces list to Monday sessions and that ordering is by earliest start.
     * Data set:
     *  - Alex: Mon 12pm–1pm and Mon 5pm–6pm  -> earliest 12:00
     *  - Bernice: Mon 1pm–2pm                 -> earliest 13:00
     *  - Charlie: Tue 10am–11am               -> excluded for Monday
     */
    @Test
    public void execute_filtersByDay_andSortsByEarliestStart() {
        Student alex = student(
                "Alex",
                new Session(new Day("Mon"), new Time("12pm-1pm")),
                new Session(new Day("Mon"), new Time("5pm-6pm"))
        );
        Student bernice = student(
                "Bernice",
                new Session(new Day("Mon"), new Time("1pm-2pm"))
        );
        Student charlie = student(
                "Charlie",
                new Session(new Day("Tue"), new Time("10am-11am"))
        );

        ModelStub model = new ModelStub(FXCollections.observableArrayList(alex, bernice, charlie));

        CommandResult result = new ViewSessionCommand(DayOfWeek.MONDAY).execute(model);

        assertEquals("showing sessions on MONDAY", result.getFeedbackToUser());
        assertIterableEquals(
                FXCollections.observableArrayList(alex, bernice),
                model.getFilteredPersonList()
        );
    }

    /**
     * Build student with supplied sessions and neutral values that satisfy validators.
     * Address and phone match project constraints (pattern mirrors typical seed data).
     *
     * @param name display name
     * @param sessions session entries (Day uses Mon/Tue/...; Time uses am/pm ranges)
     * @return student instance
     */
    private static Student student(String name, Session... sessions) {
        HashSet<Session> set = new HashSet<>();
        for (Session s : sessions) {
            set.add(s);
        }
        return new Student(
                new Name(name),
                new Phone("87438807"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                new Remark(""),
                new HashSet<>(),
                set
        );
    }

    /**
     * Minimal model stub that mirrors production filtered→sorted pipeline using
     * {@link FilteredList} and {@link SortedList}. Only methods exercised by
     * {@link ViewSessionCommand} are implemented.
     */
    private static final class ModelStub implements Model {
        private final ObservableList<Person> backing;
        private final FilteredList<Person> filtered;
        private final SortedList<Person> sorted;
        private final UserPrefs prefs = new UserPrefs();

        /**
         * Construct stub with initial persons list.
         *
         * @param initial initial persons
         */
        ModelStub(ObservableList<Person> initial) {
            this.backing = initial;
            this.filtered = new FilteredList<>(backing);
            this.sorted = new SortedList<>(filtered);
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return prefs;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return prefs.getGuiSettings();
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            prefs.setGuiSettings(guiSettings);
        }

        @Override
        public Path getAddressBookFilePath() {
            return prefs.getAddressBookFilePath();
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            prefs.setAddressBookFilePath(addressBookFilePath);
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasPerson(Person person) {
            return backing.stream().anyMatch(p -> p.isSamePerson(person));
        }

        @Override
        public void deletePerson(Person target) {
            backing.remove(target);
        }

        @Override
        public void addPerson(Person person) {
            backing.add(person);
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            int idx = backing.indexOf(target);
            backing.set(idx, editedPerson);
        }

        @Override
        public void linkParent(Student student) {
            // not used here
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.unmodifiableObservableList(sorted);
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            filtered.setPredicate(predicate);
        }

        @Override
        public void sortFilteredPersonList(Comparator<Person> comparator) {
            sorted.setComparator(comparator);
        }

        /**
         * Keep legacy Optional&lt;SessionSlot&gt; paths compile-visible if other tests rely on them.
         *
         * @param p person
         * @return optional slot
         */
        @SuppressWarnings("unused")
        private Optional<SessionSlot> legacySlotFor(Person p) {
            return p.getSessionSlot();
        }
    }
}
