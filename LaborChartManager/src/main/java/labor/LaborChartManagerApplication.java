package labor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class LaborChartManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaborChartManagerApplication.class, args);
	}
	
	// Starts bot after registering class
	@EventListener(ApplicationReadyEvent.class)
	public void afterStartup() {
		System.out.println("Butt");
	}
}
