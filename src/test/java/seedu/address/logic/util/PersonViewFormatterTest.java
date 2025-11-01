package seedu.address.logic.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
import seedu.address.testutil.ParentBuilder;
import seedu.address.testutil.StudentBuilder;

public class PersonViewFormatterTest {

    @Test
    public void format_allFieldsPresent_includesRemark() {
        Person p = new StudentBuilder()
                .withRemark("Prefers morning sessions")
                .build();
        String formatted = PersonViewFormatter.format(p);
        assertTrue(formatted.contains("Name: "));
        assertTrue(formatted.contains("Remark: Prefers morning sessions"));
    }

    @Test
    public void format_emptyRemark_replacesWithDash() {
        Person p = new StudentBuilder()
                .withRemark("") // not null; empty allowed by model
                .build();
        String formatted = PersonViewFormatter.format(p);
        assertTrue(formatted.contains("Remark: -"));
    }

    @Test
    public void format_parentWithNoChildren_showsNoChildren() {
        Parent parent = new ParentBuilder().build();
        String formatted = PersonViewFormatter.format(parent);
        assertTrue(formatted.contains("Children: -"));
    }

    @Test
    public void format_studentWithParent_showsParentName() {
        Parent parent = new ParentBuilder().withName("A Smith").build();
        Student student = new StudentBuilder()
                .withName("Alice")
                .withParentName(parent.getName().fullName)
                .build();

        String formatted = PersonViewFormatter.format(student);

        assertAll(() -> assertTrue(formatted.contains("Parent: A Smith"),
                "Should contain parent's name"), () -> assertTrue(formatted.contains("Name: Alice"),
                "Should contain student's name")
        );
    }

    @Test
    public void format_studentWithoutParent_showsDashForParent() {
        Student student = new StudentBuilder()
                .withName("Alice")
                .build();

        String formatted = PersonViewFormatter.format(student);
        assertTrue(formatted.contains("Parent: -"),
            "Should show dash for missing parent");
    }
}
