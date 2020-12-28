package labor.Command.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import labor.Command.Command;
import labor.Command.CommandInfo;
import labor.Entity.Cooper;
import labor.Entity.Position;
import labor.Repositories.PositionRepository;
import labor.Service.RepoService;
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class CreateCooperCommand implements Command {
	
	@CommandInfo(name="create_cooper", expectedArgs=1)
	@Override
	public void execute(Message message, DiscordOutput discordOutput, RepoService repoService) {
		System.out.println("Preparing to add new member.");
		List<Member> memberList = message.getMentionedMembers();
		
		List<Cooper> cooperList = memberList.stream()
			.map(member -> new Cooper(member.getNickname(), member.getUser().getAsMention()))
			.collect(Collectors.toList());
		
		StringBuilder returnMessage = new StringBuilder();
		for(Cooper cooper:cooperList) {
			repoService.getMemberRepo().save(cooper);
			returnMessage.append(cooper.getDiscordTag() + ", ");
		}
		
		returnMessage.insert(0, "These new members have been added to the database: \n");
		discordOutput.sendMessage(returnMessage.toString());
	}
	
}

