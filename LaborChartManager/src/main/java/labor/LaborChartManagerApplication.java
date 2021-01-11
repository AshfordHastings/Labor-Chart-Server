package labor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import labor.Service.LaborService;

@SpringBootApplication
public class LaborChartManagerApplication {
	@Autowired
	LaborService laborService;
	

	public static void main(String[] args) {
		SpringApplication.run(LaborChartManagerApplication.class, args);
	}
	
	// Starts bot after registering class
	@EventListener(ApplicationReadyEvent.class)
	public void afterStartup() {
		laborService.getJdaService().startBot();
	}
}
