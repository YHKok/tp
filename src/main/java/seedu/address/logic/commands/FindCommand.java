package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.RoleContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the
 * argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-insensitive) and of a certain role"
            + " and displays them as a list with index numbers.\n"
            + "Parameters: " + "[" + PREFIX_NAME + "NAME" + "]" + "[" + PREFIX_ROLE + "ROLE" + "]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "alice " + PREFIX_ROLE + "student";

    private final NameContainsKeywordsPredicate namePredicate;
    private final RoleContainsKeywordsPredicate rolePredicate;
    private final TagContainsKeywordsPredicate tagPredicate;

    /**
     * Creates a FindCommand to filter persons by the given name and role predicates.
     *
     * @param namePredicate predicate used to filter persons by name
     * @param rolePredicate predicate used to filter persons by role
     */
    public FindCommand(NameContainsKeywordsPredicate namePredicate, RoleContainsKeywordsPredicate rolePredicate,
                       TagContainsKeywordsPredicate tagPredicate) {
        this.namePredicate = namePredicate;
        this.rolePredicate = rolePredicate;
        this.tagPredicate = tagPredicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Predicate<Person> predicate;
        if (this.namePredicate.isEmpty() && this.rolePredicate.isEmpty() && this.tagPredicate.isEmpty()) {
            predicate = person -> false;
        } else {
            predicate = person ->true;
            if (!this.namePredicate.isEmpty()) {
                predicate = predicate.and(this.namePredicate);
            }
            if (!this.rolePredicate.isEmpty()) {
                predicate = predicate.and(this.rolePredicate);
            }
            if (!this.tagPredicate.isEmpty()) {
                predicate = predicate.and(this.tagPredicate);
            }
        }
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;

        boolean isNameEqual;
        if (this.namePredicate == null && otherFindCommand.namePredicate == null) {
            isNameEqual = true;
        } else if (this.namePredicate == null || otherFindCommand.namePredicate == null) {
            isNameEqual = false;
        } else {
            isNameEqual = this.namePredicate.equals(otherFindCommand.namePredicate);
        }

        boolean isRoleEqual;
        if (this.rolePredicate == null && otherFindCommand.rolePredicate == null) {
            isRoleEqual = true;
        } else if (this.rolePredicate == null || otherFindCommand.rolePredicate == null) {
            isRoleEqual = false;
        } else {
            isRoleEqual = this.rolePredicate.equals(otherFindCommand.rolePredicate);
        }

        boolean isTagEqual;

        if (this.tagPredicate == null && otherFindCommand.tagPredicate == null) {
            isTagEqual = true;
        } else if (this.tagPredicate == null || otherFindCommand.tagPredicate == null) {
            isTagEqual = false;
        } else {
            isTagEqual = this.tagPredicate.equals(otherFindCommand.tagPredicate);
        }

        return isNameEqual && isRoleEqual && isTagEqual;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name predicate", this.namePredicate)
                .add("role predicate", this.rolePredicate)
                .add("tag predicate", this.tagPredicate)
                .toString();
    }
}
