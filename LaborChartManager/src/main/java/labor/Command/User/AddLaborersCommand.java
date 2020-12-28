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
import labor.Service.RepoService;
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class AddLaborersCommand implements Command {
	@CommandInfo(name="add_laborers")
	@Override
	public void execute(Message message, DiscordOutput discordOutput, RepoService repoService) {
		
		// !add_laborers (DD OR Dish_Dog) Thursday @Ben @Ash
		System.out.println("Preparing to update a labor slot.");
		
		List<String> messageParsed = Stream.of(message.getContentDisplay().split(" "))
				.map(str -> new String(str))
				.collect(Collectors.toList());
		
		String positionString = messageParsed.get(1);
		DayOfWeek dayOfWeek = DayOfWeek.valueOf(messageParsed.get(2).toUpperCase());
		List<Member> memberList = message.getMentionedMembers();
		
		List<LaborSlot> matchingSlots = repoService.getLaborSlotRepo().findByDayOfWeekAndPosition(dayOfWeek, positionString);
		
		if(matchingSlots.size() > memberList.size()) {
			discordOutput.sendMessage("You put too many laborers! You must be dumb!");
			return;
		}
		
		List<LaborSlot> emptyLaborSlots = matchingSlots.stream()
				.filter(slot -> slot.getMember() == null)
				.collect(Collectors.toList());
		
		if(emptyLaborSlots.size() > memberList.size()) {
			StringBuilder returnString = new StringBuilder();
			returnString.append("Whoops! You've gotta delete some laborers first before you add these new ones! \n");
			returnString.append("Here are the current laborers working: \n");
			for(LaborSlot slot : emptyLaborSlots) {
				returnString.append(slot.getMember().getDiscordTag() + '\n');
			}
			discordOutput.sendMessage(returnString.toString());
			return;
		}
		
		StringBuilder returnMessage = new StringBuilder();
		
		for(int i = 0; i < memberList.size(); i++) {
				Cooper updatedCooper;
				Member memberEntry = memberList.get(i);
				LaborSlot updateSlot = matchingSlots.get(i);
				
				List<Cooper> cooperSearchByTag = repoService.getMemberRepo().findByDiscordTag(memberEntry.getUser().getAsMention());
				if(!(cooperSearchByTag.size() > 0)) {
					updatedCooper = new Cooper(memberEntry.getUser().getAsMention());
					repoService.getMemberRepo().save(updatedCooper);
				} else {
					updatedCooper = cooperSearchByTag.get(0);
				}
				returnMessage.append(updatedCooper.getDiscordTag() + '\n');
				updateSlot.setCooper(updatedCooper);
				repoService.getLaborSlotRepo().save(updateSlot);
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
