package labor.Command.User;

import labor.Command.Command;
import labor.Command.CommandInfo;
import labor.Service.LaborService;
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.entities.Message;


public class StartBotCommand implements Command {
	@CommandInfo(name="start_bot")
	@Override
	public void execute(Message message, DiscordOutput discordOutput, LaborService laborService) {
		// TODO Auto-generated method stub
		
		laborService.getJdaService().setDefaultOutput(message.getChannel());
		laborService.getJdaService().activateLaborCommands();
		
	}

}
