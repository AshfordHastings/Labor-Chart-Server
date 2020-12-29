package labor.Command.User;

import java.util.List;

import labor.Command.Command;
import labor.Command.CommandInfo;
import labor.Entity.Cooper;
import labor.Entity.LaborSlot;
import labor.Service.LaborService;
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class GetCooperCommand implements Command{
	@CommandInfo(name="get_cooper")
	@Override
	public void execute(Message message, DiscordOutput discordOutput, LaborService laborService) {
		List<Member> membersToGet = message.getMentionedMembers();
		
		for(Member member : membersToGet) {
			try {
				Cooper cooperToQuery = laborService.getRepoService().getMemberRepo().findByDiscordTag(member.getAsMention()).get(0);
				List<LaborSlot> laborSlotsQueried = laborService.getRepoService().getLaborSlotRepo().findByCooper(cooperToQuery);
				if(laborSlotsQueried.size() < 1) {
					discordOutput.sendMessage("Member " + member.getAsMention() + " has no labor slots in the database! They must be dumb!");
				} else {
					StringBuilder returnString = new StringBuilder();
					returnString.append(member.getAsMention() + ": \n");
					for(LaborSlot laborSlot : laborSlotsQueried) {
						returnString.append(laborSlot);
					}
					discordOutput.sendMessage(returnString.toString());
				}
			} catch(IndexOutOfBoundsException e) {
				discordOutput.sendMessage("Member " + member.getAsMention() + " has no record in the database! You must be dumb!");
			}
		}	
	}
}
