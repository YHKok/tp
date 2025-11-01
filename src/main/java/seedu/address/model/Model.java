package seedu.address.model;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * API for model component.
 * Keeps address book state plus user prefs, and exposes filtered list.
 */
public interface Model {

    /** Always-true predicate to show all persons. */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    // ---------- UserPrefs ----------

    /**
     * Replace prefs with given read-only view.
     * @param userPrefs prefs; not null
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Accessor for current prefs.
     * @return prefs view
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Accessor for GUI settings.
     * @return GUI settings
     */
    GuiSettings getGuiSettings();

    /**
     * Update GUI settings.
     * @param guiSettings settings; not null
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Accessor for book file path.
     * @return path
     */
    Path getAddressBookFilePath();

    /**
     * Update book file path.
     * @param addressBookFilePath path; not null
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    // ---------- AddressBook data ----------

    /**
     * Replace in-memory book with given read-only view.
     * @param addressBook data; not null
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /**
     * Accessor for in-memory book.
     * @return read-only view
     */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Check presence of person.
     * @param person target; not null
     * @return true if present
     */
    boolean hasPerson(Person person);

    /**
     * Remove person from book.
     * @param target person; not null
     */
    void deletePerson(Person target);

    /**
     * Add person to book.
     * @param person person; not null
     */
    void addPerson(Person person);

    /**
     * Replace existing person with edited copy.
     * @param target original; not null
     * @param editedPerson replacement; not null
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Link parent to student record.
     * Called by AddCommand post-create.
     * @param student target; not null
     */
    void linkParent(Student student);

    // ---------- Filter / sort / expose ----------

    /**
     * Update predicate used by filtered list.
     * @param predicate filter; not null
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Sort current filtered list with comparator; stable inside equal keys.
     * Allows commands to order output without mutating stored data.
     * @param comparator order; not null
     */
    void sortFilteredPersonList(Comparator<Person> comparator);

    /**
     * Unmodifiable view of current filtered (and possibly sorted) list.
     * @return observable list
     */
    ObservableList<Person> getFilteredPersonList();
}
