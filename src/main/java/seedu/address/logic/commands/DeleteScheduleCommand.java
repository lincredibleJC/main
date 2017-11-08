package seedu.address.logic.commands;

import javafx.collections.ObservableList;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.model.ScheduleListChangedEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.schedule.Schedule;
import seedu.address.model.schedule.exceptions.ScheduleNotFoundException;

//@@author limcel
/**
 * Deletes a schedule identified using it's last displayed index from the address book.
 */
public class DeleteScheduleCommand extends Command {

    public static final String COMMAND_WORD = "deleteschedule";
    public static final String COMMAND_ALIAS = "dsch";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the schedule identified by the index number.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_SCHEDULE_SUCCESS = "Deleted Schedule: %1$s";

    private final Index targetIndex;

    public DeleteScheduleCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        ObservableList<Schedule> lastShownList = model.getScheduleList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SCHEDULE_DISPLAYED_INDEX);
        }

        Schedule scheduleToDelete = lastShownList.get(targetIndex.getZeroBased());

        try {
            model.removeSchedule(scheduleToDelete);
            EventsCenter.getInstance().post(new ScheduleListChangedEvent(model.getScheduleList()));
        } catch (ScheduleNotFoundException e) {
            assert false : "The target schedule cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_DELETE_SCHEDULE_SUCCESS, scheduleToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteScheduleCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteScheduleCommand) other).targetIndex)); // state check
    }

}
