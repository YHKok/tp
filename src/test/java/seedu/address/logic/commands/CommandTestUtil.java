package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NEW_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Day;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Session;
import seedu.address.model.person.Time;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_NAME_CHARLES = "Charles Kwan";
    public static final String VALID_NAME_IVAN = "Ivan Sim";
    public static final String VALID_NAME_JAMES = "James Lee";
    public static final String VALID_NAME_KELLY = "Kelly Khoo";
    public static final String VALID_PHONE_AMY = "98765432";
    public static final String VALID_PHONE_BOB = "87654321";
    public static final String VALID_PHONE_CHARLES = "91234453";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1, Singapore 654321";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3, Singapore 678901";
    public static final String VALID_ADDRESS_CHARLES = "Block 456, Charles Street 55, Singapore 676767";
    public static final String VALID_ROLE_STUDENT = "student";
    public static final String VALID_ROLE_PARENT = "parent";
    public static final String VALID_ROLE_AMY = VALID_ROLE_STUDENT;
    public static final String VALID_ROLE_BOB = VALID_ROLE_STUDENT;
    public static final String VALID_TAG_SUBJECT = "math";
    public static final String VALID_TAG_SUBJECT2 = "science";
    public static final String VALID_REMARK_AMY = "Likes Math.";
    public static final String VALID_REMARK_BOB = "Favourite pastime: Teaching";
    public static final String VALID_REMARK_CHARLES = "I love my children.";
    public static final String VALID_DAY = "Mon";
    public static final String VALID_TIME = "12pm-1pm";
    public static final String VALID_DAY_EXTRA = "Tue";
    public static final String VALID_TIME_EXTRA = "9am-1pm";
    public static final String VALID_DAY_MON = "Monday";
    public static final String VALID_TIME_1PM = "1pm-2pm";
    public static final String VALID_DAY_TUE = "Tuesday";
    public static final String VALID_TIME_2PM = "2pm-3pm";
    public static final Session VALID_SESSION = new Session(new Day(VALID_DAY), new Time(VALID_TIME));

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String NAME_DESC_CHARLES = " " + PREFIX_NAME + VALID_NAME_CHARLES;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String PHONE_DESC_CHARLES = " " + PREFIX_PHONE + VALID_PHONE_CHARLES;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String ADDRESS_DESC_CHARLES = " " + PREFIX_ADDRESS + VALID_ADDRESS_CHARLES;
    public static final String ROLE_DESC_STUDENT = " " + PREFIX_ROLE + VALID_ROLE_STUDENT;
    public static final String ROLE_DESC_PARENT = " " + PREFIX_ROLE + VALID_ROLE_PARENT;
    public static final String ROLE_DESC_AMY = " " + PREFIX_ROLE + VALID_ROLE_AMY;
    public static final String ROLE_DESC_BOB = " " + PREFIX_ROLE + VALID_ROLE_BOB;
    public static final String TAG_DESC_SUBJECT = " " + PREFIX_TAG + VALID_TAG_SUBJECT;
    public static final String TAG_DESC_SUBJECT2 = " " + PREFIX_TAG + VALID_TAG_SUBJECT2;
    public static final String DAY_DESC = " " + PREFIX_DAY + VALID_DAY;
    public static final String TIME_DESC = " " + PREFIX_TIME + VALID_TIME;
    public static final String DAY_DESC_EXTRA = " " + PREFIX_DAY + VALID_DAY_EXTRA;
    public static final String TIME_DESC_EXTRA = " " + PREFIX_TIME + VALID_TIME_EXTRA;
    public static final String PARENT_DESC_CHARLES = " " + PREFIX_PARENT + VALID_NAME_CHARLES;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_ROLE_DESC = " " + PREFIX_ROLE + "friend"; // 'hubby' not allowed in tags
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "p@rent"; // 'hubby' not allowed in tags
    public static final String INVALID_DAY_DESC = " " + PREFIX_DAY + "Mondayy";
    public static final String INVALID_NEW_DAY_DESC = " " + PREFIX_NEW_DAY + "Mondayy";
    public static final String INVALID_TIME_DESC = " " + PREFIX_TIME + "1200";
    public static final String INVALID_NEW_TIME_DESC = " " + PREFIX_NEW_TIME + "1200";

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;
    public static final EditCommand.EditPersonDescriptor DESC_CHARLES;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withAddress(VALID_ADDRESS_AMY)
                .withRole(VALID_ROLE_AMY) // Can be removed?
                .withRemark(VALID_REMARK_AMY)
                .withTags(VALID_TAG_SUBJECT).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withAddress(VALID_ADDRESS_BOB)
                .withRole(VALID_ROLE_BOB) // Can be removed?
                .withRemark(VALID_REMARK_BOB)
                .withTags(VALID_TAG_SUBJECT).build();
        DESC_CHARLES = new EditPersonDescriptorBuilder().withName(VALID_NAME_CHARLES)
                .withPhone(VALID_PHONE_CHARLES).withAddress(VALID_ADDRESS_CHARLES)
                .withRemark(VALID_REMARK_CHARLES)
                .build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

}
