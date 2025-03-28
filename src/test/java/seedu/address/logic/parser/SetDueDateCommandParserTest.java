package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DUE_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_INDEX;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SetDueDateCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Test class for SetDueDateCommandParser.
 */
public class SetDueDateCommandParserTest {

    private final SetDueDateCommandParser parser = new SetDueDateCommandParser();

    @Test
    public void parse_validInput_success() throws Exception {
        String userInput = "1 " + PREFIX_TASK_INDEX + "2 " + PREFIX_DUE_DATE + "2026-03-30 14:00";
        SetDueDateCommand expectedCommand = new SetDueDateCommand(
                LocalDateTime.parse("2026-03-30T14:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                Index.fromOneBased(2),
                Index.fromOneBased(1));
        assertEquals(expectedCommand, parser.parse(userInput));
    }

    @Test
    public void parse_missingTaskIndex_throwsParseException() {
        String userInput = "1 " + PREFIX_DUE_DATE + "2026-03-30 14:00";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_missingDueDate_throwsParseException() {
        String userInput = "1 " + PREFIX_TASK_INDEX + "2";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidDateFormat_throwsParseException() {
        String userInput = "1 " + PREFIX_TASK_INDEX + "2 " + PREFIX_DUE_DATE + "30-03-2026";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_pastDate_throwsParseException() {
        String userInput = "1 " + PREFIX_TASK_INDEX + "2 " + PREFIX_DUE_DATE + "2023-03-30 14:00";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_incorrectPrefix_throwsParseException() {
        String userInput = "1 " + "/taskint" + "2 " + PREFIX_DUE_DATE + "2026-03-30 14:00";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}

