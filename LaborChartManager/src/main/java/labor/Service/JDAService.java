package labor.Service;

import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import labor.Command.Command;
import labor.Command.CommandService;
import labor.Command.User.AddLaborersCommand;
import labor.Command.User.CreateCooperCommand;
import labor.Command.User.CreateEntityCommand;
import labor.Command.User.GetCooperCommand;
import labor.Command.User.GetLaborTimeCommand;
import labor.Command.User.RemoveLaborersCommand;
import labor.Command.User.StartBotCommand;
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;

@Service
@PropertySource("classpath:application.yaml")
public class JDAService {
	@Autowired
	CommandService commandService;
	
	@Autowired
	LaborService laborService;
	
	@Value("${JDA.token}")
	String token;
	
	@Value("${JDA.testing-channel}")
	String testingChannelId;
	
	JDA jda;
	DiscordOutput defaultOutput;
	
	public void startBot() {
		try {
			System.out.println("About to create bot with token: " + token);
			this.jda = JDABuilder.createDefault(token)
				.setEventManager(new AnnotatedEventManager())
				.build();
			System.out.println("Bot has been connected");
			
			addCommands(new StartBotCommand());
			
		} catch (LoginException e) {
			System.out.println("Bot can't connect! EEK!");
			e.printStackTrace();
		}
	}
	
	public void activateLaborCommands() {
		commandService.addEventListeners(
				new CreateEntityCommand(),
				new CreateCooperCommand(),
				new AddLaborersCommand(),
				new RemoveLaborersCommand(),
				new GetCooperCommand(),
				new GetLaborTimeCommand());
	}
	
	
	public void addCommands(Command... commands) {
		List<Command> commandList = Arrays.asList(commands);
		commandService.addEventListeners(commandList);
	}
	

	public JDA getJDA() {
		return jda;
	}
	
	public void setDefaultOutput(MessageChannel defaultChannel) {
		this.defaultOutput = new DiscordOutput(defaultChannel);
		defaultChannel.sendMessage("Default Channel set!");
		
	}
	
	public DiscordOutput getDefaultOutput() {
		return defaultOutput;
	}
	
	public CommandService getCommandRegistry() {
		return commandService;
	}
	
}
