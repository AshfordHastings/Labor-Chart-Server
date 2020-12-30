package labor.Command.User;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import labor.Command.Command;
import labor.Command.CommandInfo;
import labor.Entity.Cooper;
import labor.Entity.LaborSlot;
import labor.Service.LaborService;
import labor.Service.RepoService;
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class AddLaborersCommand implements Command {
	@CommandInfo(name="add_laborers")
	@Override
	public void execute(Message message, DiscordOutput discordOutput, LaborService laborService) {
		
		// !add_laborers (DD OR Dish_Dog) Thursday @Ben @Ash
		System.out.println("Preparing to add laborers to a labor slot.");
		
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
		
		List<Member> memberList = message.getMentionedMembers();
		
		List<LaborSlot> dbSlotsFromQuery = laborService.getRepoService().getLaborSlotRepo().findByDayOfWeekAndPosition(dayOfWeek, positionString);
		
		if(!(dbSlotsFromQuery.size() >= memberList.size())) {
			StringBuilder returnString = new StringBuilder();
			returnString.append("You put too many laborers! You must be dumb!\n");
			returnString.append("Number of laborers working at " + dayOfWeek.toString() + " " + positionString + ": " + dbSlotsFromQuery.size());
			discordOutput.sendMessage(returnString.toString());
			return;
		}
		
		List<LaborSlot> emptyLaborSlots = dbSlotsFromQuery.stream()
				.filter(slot -> slot.getMember() == null)
				.collect(Collectors.toList());
		
		if(!(emptyLaborSlots.size() >= memberList.size())) {
			StringBuilder returnString = new StringBuilder();
			returnString.append("Whoops! You've gotta delete some laborers first before you add these new ones! \n");
			returnString.append("Here are the current laborers working: \n");
			for(LaborSlot slot : dbSlotsFromQuery) {
				if(slot.getMember() != null) {
					returnString.append(slot.getMember().getDiscordTag() + '\n');
				}
			}
			discordOutput.sendMessage(returnString.toString());
			return;
		}
		
		StringBuilder returnMessage = new StringBuilder();
		
		for(int i = 0; i < memberList.size(); i++) {
				Cooper updatedCooper;
				Member memberEntry = memberList.get(i);
				
				try {
					LaborSlot updateSlot = emptyLaborSlots.get(i);
					List<Cooper> cooperSearchByTag = laborService.getRepoService().getMemberRepo().findByDiscordTag(memberEntry.getUser().getAsMention());
					if(!(cooperSearchByTag.size() > 0)) {
						updatedCooper = new Cooper(memberEntry);
						laborService.getRepoService().getMemberRepo().save(updatedCooper);
					} else {
						updatedCooper = cooperSearchByTag.get(0);
					}
					returnMessage.append(updatedCooper.getDiscordTag() + '\n');
					updateSlot.setCooper(updatedCooper);
					laborService.getRepoService().getLaborSlotRepo().save(updateSlot);
				}
				catch(IndexOutOfBoundsException e) {
					
					discordOutput.sendMessage("You added to many laborers to your !create_laborers command!");
				}
				

		}
		/*
		StringBuilder returnMessage = new StringBuilder();
		for(int i = 0; i < newLaborers.size(); i++) {
			LaborSlot slot = matchingSlots.get(i);
			Cooper cooper = newLaborers.get(i);
			returnMessage.append(cooper.getDiscordTag() + '\n');
			slot.setCooper(cooper);
			repoService.getLaborSlotRepo().save(slot);
		}
		*/
		
		returnMessage.insert(0, "The " + dayOfWeek.toString() + " " + positionString + " position has been updated!: \n");
		discordOutput.sendMessage(returnMessage.toString());
	}
}
