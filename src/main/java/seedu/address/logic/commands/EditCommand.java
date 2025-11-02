package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_NO_PARENT_FOR_PARENT;
import static seedu.address.logic.Messages.MESSAGE_NO_TAGS_FOR_PARENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Role;
import seedu.address.model.person.Session;
import seedu.address.model.person.Student;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_PARENT + "PARENT] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 ";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in EduConnect.";
    public static final String MESSAGE_INVALID_PARENT = "This parent does not exist in EduConnect.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        // Do not allow updating of Role or Remark

        if (personToEdit instanceof Student studentToEdit) {
            Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(studentToEdit.getTags());
            Set<Session> updatedSessions = editPersonDescriptor.getSessions().orElse(studentToEdit.getSessions());
            Name updatedParentName = editPersonDescriptor.getParentName().orElse(studentToEdit.getParentName());
            return new Student(updatedName, updatedPhone, updatedAddress, personToEdit.getRemark(),
                    updatedTags, updatedSessions, updatedParentName);
        } else {
            return new Parent(updatedName, updatedPhone, updatedAddress, personToEdit.getRemark());
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        // Validate using editPersonDescriptor, if we use editedPerson some data might be lost after parsing
        validateRoleInputs(personToEdit, editPersonDescriptor, model);

        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    private void validateRoleInputs(Person personToEdit, EditPersonDescriptor editPersonDescriptor,
                                    Model model) throws CommandException {
        // Unable to do role based validation in the Parser due to
        // being unable to check the role of the person we are editing.
        // So we shall do it here instead.
        if (personToEdit instanceof Parent parent) {
            if (editPersonDescriptor.getParentName().isPresent()) {
                throw new CommandException(MESSAGE_NO_PARENT_FOR_PARENT);
            }
            if (editPersonDescriptor.getTags().isPresent()) {
                throw new CommandException(MESSAGE_NO_TAGS_FOR_PARENT);
            }
        }

        if (personToEdit instanceof Student student) {
            // Check if student already has parent name
            // and if existing parent name is diff from new one
            // and if new parent exists
            if (student.getParentName() != null
                    && editPersonDescriptor.getParentName().isPresent()
                    && !student.getParentName().equals(editPersonDescriptor.getParentName().get())
                    && !model.hasPerson(new Parent(editPersonDescriptor.getParentName().get()))) {
                throw new CommandException(MESSAGE_INVALID_PARENT);
            } else if (student.getParentName() == null && editPersonDescriptor.getParentName().isPresent()
                    && !model.hasPerson(new Parent(editPersonDescriptor.getParentName().get()))) {
                throw new CommandException(MESSAGE_INVALID_PARENT);
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Address address;
        private Role role; // Only used for Testing
        private Remark remark; // Only used for Testing
        private Set<Tag> tags;
        private Set<Session> sessions;
        private Name parentName;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setAddress(toCopy.address);
            setRole(toCopy.role);
            setRemark(toCopy.remark);
            setTags(toCopy.tags);
            setSessions(toCopy.sessions);
            setParentName(toCopy.parentName);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, address, tags, parentName);
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Name> getParentName() {
            return Optional.ofNullable(parentName);
        }

        public void setParentName(Name parentName) {
            this.parentName = parentName;
        }

        public Optional<Role> getRole() {
            return Optional.ofNullable(role);
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public Optional<Remark> getRemark() {
            return Optional.ofNullable(remark);
        }

        public void setRemark(Remark remark) {
            this.remark = remark;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Session>> getSessions() {
            return (this.sessions != null) ? Optional.of(Collections.unmodifiableSet(sessions)) : Optional.empty();
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setSessions(Set<Session> sessions) {
            this.sessions = (sessions != null) ? new HashSet<>(sessions) : null;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(parentName, otherEditPersonDescriptor.parentName)
                    && Objects.equals(role, otherEditPersonDescriptor.role)
                    && Objects.equals(remark, otherEditPersonDescriptor.remark)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags);
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
}
