package labor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import labor.Command.CommandService;
import labor.Command.User.AddLaborersCommand;
import labor.Command.User.CreateCooperCommand;
import labor.Command.User.CreateEntityCommand;
import labor.Command.User.RemoveLaborersCommand;
import labor.Service.JDAService;
import labor.Util.DiscordOutput;

@SpringBootApplication
public class LaborChartManagerApplication {
	@Autowired
	JDAService jdaService;
	@Autowired
	CommandService commandService;
	

	public static void main(String[] args) {
		SpringApplication.run(LaborChartManagerApplication.class, args);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void afterStartup() {
		jdaService.startBot();
		commandService.addEventListeners(
				new CreateEntityCommand(),
				new CreateCooperCommand(),
				new AddLaborersCommand(),
				new RemoveLaborersCommand());
		
		DiscordOutput defaultOutput = jdaService.getDefaultOutput();
		//defaultOutput.sendMessage("Bot is connected!");
	}
}
