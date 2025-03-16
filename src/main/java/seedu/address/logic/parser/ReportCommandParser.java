package seedu.address.logic.parser;

import seedu.address.logic.commands.ReportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ReportCommand object.
 */
public class ReportCommandParser implements Parser<ReportCommand> {
    @Override
    public ReportCommand parse(String args) throws ParseException {
        return new ReportCommand();
    }
}
