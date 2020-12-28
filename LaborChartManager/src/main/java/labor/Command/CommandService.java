package labor.Command;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import labor.Service.JDAService;
import labor.Service.RepoService;
import labor.Util.DiscordOutput;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

@Service
public class CommandService {
	@Autowired
	JDAService jdaService;
	
	@Autowired
	RepoService repoService;
	
	
	
	public void addEventListeners(Command... commands) {
		addEventListeners(Arrays.asList(commands));
	}
	
	public void addEventListeners(List<Command> commands) {
		for(Command command : commands) {
			Method[] methods = command.getClass().getMethods();
			
			for(Method method : methods) {
				CommandInfo info;
				try {
					info = method.getAnnotation(CommandInfo.class);
				} catch(NullPointerException e) {
					continue;
				}
				if(info==null) {continue;}
				
				System.out.println("Preparing to add command : " + info.name());
				
				this.jdaService.getJDA().addEventListener(new AnnotatedEventManager() {
					@SubscribeEvent
					public void registerCommand(MessageReceivedEvent event) {
						Message message = event.getMessage();
						String messageString = message.getContentDisplay();
						
						if(!messageString.startsWith("!")) {
							return;
						}
						
						List<String> messageParsed = Stream.of(messageString.split(" "))
								.map(str -> new String(str))
								.collect(Collectors.toList());
						
						if(!messageParsed.get(0).substring(1).equals(info.name())) {
							return;
						}
						
						
						DiscordOutput responseOutput = new DiscordOutput(event.getChannel());
						if(info.expectedArgs() != -1 && info.expectedArgs() != messageParsed.size() - 1) {
							responseOutput.sendMessage("Unexpected arguement count! Expected argument count is:" + info.expectedArgs() + ".");
							return;
						}
						
						command.execute(message, responseOutput, repoService);
					}
				});
				System.out.println("New command added: " + info.name());
			}
		}
	}
}
