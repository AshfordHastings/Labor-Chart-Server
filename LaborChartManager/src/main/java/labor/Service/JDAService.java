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
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;

@Service
@PropertySource("classpath:application.yaml")
public class JDAService {
	@Autowired
	CommandService commandService;
	
	@Value("${JDA.token}")
	String token;
	
	@Value("${JDA.testing-channel}")
	String testingChannelId;
	
	JDA jda;
	
	public void startBot() {
		try {
			System.out.println("About to create bot with token: " + token);
			this.jda = JDABuilder.createDefault(token)
				.setEventManager(new AnnotatedEventManager())
				.build();
			System.out.println("Bot has been connected");
		} catch (LoginException e) {
			System.out.println("Bot can't connect! EEK!");
			e.printStackTrace();
		}
	}
	
	public void addCommands(Command... commands) {
		List<Command> commandList = Arrays.asList(commands);
		commandService.addEventListeners(commandList);
	}
	

	public JDA getJDA() {
		return jda;
	}
	
	public DiscordOutput getDefaultOutput() {
		return new DiscordOutput(jda.getTextChannelById(Long.valueOf(testingChannelId)));
	}
	
	public CommandService getCommandRegistry() {
		return commandService;
	}
	
}
