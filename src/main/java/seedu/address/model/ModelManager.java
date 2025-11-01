package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Default implementation for Model.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;

    // Backing list from book
    private final FilteredList<Person> filteredPersons;
    // Sorted view layered on top of filtered view
    private final SortedList<Person> sortedPersons;

    /**
     * Construct from read-only views. Wrap into mutable copies for in-memory work.
     * @param addressBook source; not null
     * @param userPrefs prefs view; not null
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireNonNull(addressBook);
        requireNonNull(userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);

        this.filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        this.sortedPersons = new SortedList<>(filteredPersons);
    }

    /**
     * Convenience ctor.
     */
    public ModelManager(AddressBook addressBook, UserPrefs userPrefs) {
        this((ReadOnlyAddressBook) addressBook, (ReadOnlyUserPrefs) userPrefs);
    }

    /**
     * No-arg constructor for default state.
     */
    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    // ---------- UserPrefs ----------

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    // ---------- AddressBook data ----------

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        requireNonNull(addressBook);
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        requireNonNull(target);
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        requireNonNull(person);
        addressBook.addPerson(person);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(target);
        requireNonNull(editedPerson);
        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public void linkParent(Student student) {
        requireNonNull(student);
        // Delegate to addressBook if available; keep safe no-op otherwise.
        addressBook.linkParent(student);
    }

    @Override
    public void destroyParentLink(Student student) {
        requireNonNull(student);
        addressBook.destroyParentLink(student);
    }

    @Override
    public void destroyStudentLinks(Parent parent) {
        requireNonNull(parent);
        addressBook.destroyStudentLinks(parent);
    }

    // ---------- Filter / sort / expose ----------

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public void sortFilteredPersonList(Comparator<Person> comparator) {
        requireNonNull(comparator);
        // Sort by setting comparator on SortedList wrapper; no in-place mutation on FilteredList.
        sortedPersons.setComparator(comparator);
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        // Expose sorted view; unmodifiable wrapper preserves UI contract.
        return FXCollections.unmodifiableObservableList(sortedPersons);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ModelManager)) {
            return false;
        }
        ModelManager o = (ModelManager) other;
        return addressBook.equals(o.addressBook) && userPrefs.equals(o.userPrefs);
    }
}
