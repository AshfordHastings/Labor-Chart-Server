package labor.Command;

import org.springframework.data.repository.CrudRepository;

import labor.Repositories.PositionRepository;
import labor.Service.LaborService;
import labor.Service.RepoService;
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.entities.Message;

public interface Command {
	public void execute(Message message, DiscordOutput discordOutput, LaborService laborService);
}
