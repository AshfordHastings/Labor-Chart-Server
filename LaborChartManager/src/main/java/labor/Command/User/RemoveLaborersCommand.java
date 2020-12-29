package labor.Command.User;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import labor.Command.Command;
import labor.Command.CommandInfo;
import labor.Entity.LaborSlot;
import labor.Service.LaborService;
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class RemoveLaborersCommand implements Command {
	
	@CommandInfo(name="remove_laborers")
	@Override
	public void execute(Message message, DiscordOutput discordOutput, LaborService laborService) {
		// TODO Auto-generated method stub
		int minimumArguments = 3;
		System.out.println("Preparing to update a labor slot.");
		List<Member> membersToUpdate = message.getMentionedMembers();
		List<String> messageParsed = Stream.of(message.getContentDisplay().split(" "))
				.map(str -> new String(str))
				.collect(Collectors.toList());
		
		String positionString;
		DayOfWeek dayOfWeek;
		try {
			positionString = messageParsed.get(1);
			dayOfWeek = DayOfWeek.valueOf(messageParsed.get(2).toUpperCase());
		} catch(IllegalArgumentException e) {
			discordOutput.sendMessage("Invalid day of the week entered! You must be dumb!");
			return;
		} catch(IndexOutOfBoundsException e) {
			discordOutput.sendMessage("Not enough arguements for the command add_laborers! You must be dumb!");
			return;
		}
		
		// If there are no arguments for specific laboers to be removed, remove all laborers
		if(!(messageParsed.size() > minimumArguments)) {
			List<LaborSlot> dbSlotsFromQuery = laborService.getRepoService().getLaborSlotRepo().findByDayOfWeekAndPosition(dayOfWeek, positionString);
			for(LaborSlot laborSlotFromQuery : dbSlotsFromQuery) {
				laborSlotFromQuery.clearCooper();
				laborService.getRepoService().getLaborSlotRepo().save(laborSlotFromQuery);
			}
			// If there are arguments for specific laborers to be remvoed
		} else {
			for(int i = 0; i < membersToUpdate.size(); i++) {
				try {
					LaborSlot laborSlotFromQuery = laborService.getRepoService().getLaborSlotRepo().findByDayOfWeekAndPositionAndDiscordTag(dayOfWeek, positionString, membersToUpdate.get(i).getUser().getAsTag()).get(0);
					laborSlotFromQuery.clearCooper();
					laborService.getRepoService().getLaborSlotRepo().save(laborSlotFromQuery);
				} catch(IndexOutOfBoundsException e) {
					discordOutput.sendMessage("Unable to Remove the user " + membersToUpdate.get(i).getAsMention() + " because they aren't at this labor slot!");
				}
			}
		}
		
		List<LaborSlot> dbSlotsFromQuery = laborService.getRepoService().getLaborSlotRepo().findByDayOfWeekAndPosition(dayOfWeek, positionString);
		StringBuilder returnString = new StringBuilder();
		returnString.append("Database was successfully updated! \nHere are the current coopers for the position" + positionString + " on " + dayOfWeek.toString() + ":");
		for(LaborSlot dbSlotFromQuery : dbSlotsFromQuery) {
			if(dbSlotFromQuery.getMember() != null) {
				returnString.append('\n' + dbSlotFromQuery.getMember().getDiscordTag());
			}
		}
		discordOutput.sendMessage(returnString.toString());
	}

	
}
