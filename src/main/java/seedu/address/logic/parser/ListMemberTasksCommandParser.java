package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ListMemberTasksCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The {@Code Parser} for {@ListMemberTasksCommand}.
 */
public class ListMemberTasksCommandParser implements Parser<ListMemberTasksCommand> {
    @Override
    public ListMemberTasksCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        Index personIndex;
        try {
            personIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format("Invalid input: %s\n%s", pe.getMessage(),
                            ListMemberTasksCommand.MESSAGE_USAGE), pe);
        }

        return new ListMemberTasksCommand(personIndex);
    }
}
