package seedu.address.model.person;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Parent in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Parent extends Person {
    private final Set<Name> childrenNames = new HashSet<>();
    private final Set<Student> children = new HashSet<>();

    /**
     * Every field must be present and not null.
     *
     * @param name
     * @param phone
     * @param address
     * @param remark
     */
    public Parent(Name name, Phone phone, Address address, Remark remark) {
        super(name, phone, address, Role.PARENT_ROLE, remark);
    }

    /**
     * Creates dummy Parent object for filtering
     *
     * @param name
     */
    public Parent(Name name) {
        super(name, Role.PARENT_ROLE);
    }

    /**
     * Clones a parent with a new remark
     * @param parent
     * @param remark
     */
    public Parent(Parent parent, Remark remark) {
        super(parent.name, parent.phone, parent.address, Role.PARENT_ROLE, remark);
        this.childrenNames.addAll(Set.copyOf(parent.childrenNames));
        this.children.addAll(Set.copyOf(parent.children));
    }

    public boolean hasChild(Student student) {
        return children.contains(student);
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public boolean hasChildName(Name name) {
        return childrenNames.contains(name);
    }

    /**
     * Links a given Student to this Parent.
     *
     * @param student
     */
    public void addChild(Student student) {
        children.add(student);
        childrenNames.add(student.getName());
    }

    /**
     * Unlinks a given Student from this Parent
     *
     * @param student
     */
    public void removeChild(Student student) {
        if (children.isEmpty() || !children.contains(student)) {
            return;
        }

        children.remove(student);
        childrenNames.remove(student.getName());
    }

    public Set<Student> getChildren() {
        return Set.copyOf(children);
    }

    public Set<Name> getChildrenNames() {
        return Set.copyOf(childrenNames);
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
        if (!(other instanceof Parent)) {
            return false;
        }

        Parent otherPerson = (Parent) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && address.equals(otherPerson.address);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("address", address)
                .add("role", role)
                .add("remark", remark)
                .add("children", childrenNames)
                .toString();
    }
}
