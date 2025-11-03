package seedu.address.storage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
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
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    public static final String OVERLAP_SESSION_MESSAGE_FORMAT = "Person has an overlapping session";

    private final String name;
    private final String phone;
    private final String address;
    private final String role;
    private final String remark;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedSession> sessions = new ArrayList<>();
    private final String parentName;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("address") String address,
            @JsonProperty("role") String role,
            @JsonProperty("remark") String remark,
            @JsonProperty("parentName") String parentName,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("sessions") List<JsonAdaptedSession> sessions) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.remark = remark;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (sessions != null) {
            this.sessions.addAll(sessions);
        }
        this.parentName = parentName;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        address = source.getAddress().value;
        role = source.getRole().role;
        remark = source.getRemark().value;
        if (source instanceof Student s) {
            tags.addAll(s.getTags().stream()
                    .map(JsonAdaptedTag::new)
                    .collect(Collectors.toList()));
            sessions.addAll(s.getSessions().stream()
                    .map(JsonAdaptedSession::new)
                    .collect(Collectors.toList()));
            parentName = s.getParentName() != null ? s.getParentName().fullName : null;
        } else {
            parentName = null;
        }

    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        List<Session> personSessions = new ArrayList<>();
        for (JsonAdaptedSession session : sessions) {
            personSessions.add(session.toModelType());
        }

        personSessions = personSessions.stream().sorted(Comparator
                .comparingInt((Session s) -> List.of("Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun")
                        .indexOf(s.getDay().getValue()))
                .thenComparingInt(s -> s.getTime().getStartMinutes())).toList();

        for (int i = 0; i < personSessions.size() - 1; i++) {
            Session a = personSessions.get(i);
            Session b = personSessions.get(i + 1);
            if (a.isOverlap(b)) {
                throw new IllegalValueException(OVERLAP_SESSION_MESSAGE_FORMAT);
            }
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (role == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Role.class.getSimpleName()));
        }
        if (!Role.isValidRole(role)) {
            throw new IllegalValueException(Role.MESSAGE_CONSTRAINTS);
        }

        final Remark modelRemark = new Remark(remark);

        if (role.equals("student")) {
            final Set<Tag> modelTags = new HashSet<>(personTags);
            final Set<Session> modelSessions = new HashSet<>(personSessions);
            if (parentName != null) {
                final Name modelParentName = new Name(parentName);
                return new Student(modelName, modelPhone, modelAddress, modelRemark,
                        modelTags, modelSessions, modelParentName);
            } else {
                return new Student(modelName, modelPhone, modelAddress, modelRemark, modelTags, modelSessions);
            }
        } else {
            return new Parent(modelName, modelPhone, modelAddress, modelRemark);
        }
    }

}
