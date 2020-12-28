package labor.Command.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import labor.Command.Command;
import labor.Command.CommandInfo;
import labor.Entity.Position;
import labor.Repositories.PositionRepository;
import labor.Service.RepoService;
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.entities.Message;

public class CreateMemberCommand implements Command {
	
	@CommandInfo(name="create_member", expectedArgs=1)
	@Override
	public void execute(Message message, DiscordOutput discordOutput, RepoService repoService) {
		
		
		// TODO Auto-generated method stub
		List<String> messageParsed = Stream.of(message.getContentDisplay().split(" "))
				.map(str -> new String(str))
				.collect(Collectors.toList());
		
		String id = messageParsed.get(1);
		String name = messageParsed.get(2);
		String time = messageParsed.get(3);
		String labordays = messageParsed.get(4);
		String length = messageParsed.get(5);
		String numSlots = messageParsed.get(6);
		
		Position newPosition = new Position(id, name, time, labordays, Integer.valueOf(length), Integer.valueOf(numSlots));
		repoService.getPositionRepo().save(newPosition);
		
		Optional<Position> optPosition = repoService.getPositionRepo().findById(id);
		Position position = optPosition.get();
		
		String returnMessage = new String();
		returnMessage = returnMessage.concat("New Position has been saved to the database:" + '\n' + position);
		discordOutput.sendMessage(returnMessage);
	}
	
}

