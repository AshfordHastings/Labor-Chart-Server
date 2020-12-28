package labor.Command.User;

import java.util.List;
import java.util.stream.Collectors;

import labor.Command.Command;
import labor.Command.CommandInfo;
import labor.Entity.Cooper;
import labor.Service.LaborService;
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class CreateCooperCommand implements Command {
	
	@CommandInfo(name="create_cooper")
	@Override
	public void execute(Message message, DiscordOutput discordOutput, LaborService laborService) {
		System.out.println("Preparing to add new member.");
		List<Member> memberList = message.getMentionedMembers();
		
		List<Cooper> cooperList = memberList.stream()
			.map(member -> new Cooper(member))
			.collect(Collectors.toList());
		
		StringBuilder returnMessage = new StringBuilder();
		for(Cooper cooper:cooperList) {
			
			
			laborService.getRepoService().getMemberRepo().save(cooper);
			returnMessage.append(cooper.getDiscordTag() + ", ");
		}
		
		returnMessage.insert(0, "These new members have been added to the database: \n");
		discordOutput.sendMessage(returnMessage.toString());
	}
	
}

