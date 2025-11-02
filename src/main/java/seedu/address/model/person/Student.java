package seedu.address.model.person;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Student in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Student extends Person {
    protected final Set<Session> sessions;
    private final Set<Tag> tags = new HashSet<>();

    // Allow parent to be null
    private Parent myParent = null; // Mark as transient if needed in the future
    private Name parentName = null;

    /**
     * Every field must be present and not null.
     *
     * @param name
     * @param phone
     * @param address
     * @param remark
     * @param tags
     */
    public Student(Name name, Phone phone, Address address, Remark remark, Set<Tag> tags) {
        super(name, phone, address, Role.STUDENT_ROLE, remark);
        this.tags.addAll(tags);
        this.sessions = new HashSet<>();
    }

    /**
     * This is the constructor for edit command and add session command as when add command no need to add session
     * Every field must be present and not null.
     *
     * @param name
     * @param phone
     * @param address
     * @param remark
     * @param tags
     * @param sessions
     */
    public Student(Name name, Phone phone, Address address, Remark remark, Set<Tag> tags, Set<Session> sessions) {
        super(name, phone, address, Role.STUDENT_ROLE, remark);
        this.tags.addAll(tags);
        this.sessions = sessions;
    }

    /**
     * This is the constructor used for constructing a Student from JSON data
     * Every field must be present and not null.
     *
     * @param name
     * @param phone
     * @param address
     * @param remark
     * @param tags
     * @param sessions
     * @param parentName
     */
    public Student(Name name, Phone phone, Address address, Remark remark, Set<Tag> tags,
            Set<Session> sessions, Name parentName) {
        super(name, phone, address, Role.STUDENT_ROLE, remark);
        this.tags.addAll(tags);
        this.sessions = sessions;
        this.parentName = parentName;
    }

    /**
     * Creates dummy Student object for filtering
     *
     * @param name
     */
    public Student(Name name) {
        super(name, Role.STUDENT_ROLE);
        this.sessions = new HashSet<>();
    }

    /**
     * Clones a student with a new remark
     * @param student
     * @param remark
     */
    public Student(Student student, Remark remark) {
        super(student.name, student.phone, student.address, Role.STUDENT_ROLE, remark);
        this.sessions = Set.copyOf(student.getSessions());
        this.tags.addAll(Set.copyOf(student.tags));
        this.myParent = student.myParent;
        this.parentName = student.parentName;
    }
    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable session set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Session> getSessions() {
        return Collections.unmodifiableSet(sessions);
    }

    public boolean hasSession(Session session) {
        return this.sessions.contains(session);
    }

    public boolean hasParent() {
        return this.parentName != null;
    }

    public boolean hasLinkedParent() {
        return this.parentName != null && this.myParent != null;
    }

    public Name getParentName() {
        return this.parentName;
    }

    public void setParentName(Name parentName) {
        this.parentName = parentName;
    }

    public void setParent(Parent parent) {
        myParent = parent;
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Student)) {
            return false;
        }

        Student otherPerson = (Student) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && address.equals(otherPerson.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, address, role, remark, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("address", address)
                .add("role", role)
                .add("remark", remark)
                .add("tags", tags)
                .add("parent", parentName)
                .toString();
    }
}
