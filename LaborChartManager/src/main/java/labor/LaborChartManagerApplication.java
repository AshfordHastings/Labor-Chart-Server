package labor;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import labor.Entity.Cooper;
import labor.Entity.LaborSlot;
import labor.Entity.Position;
import labor.Service.RepoService;

@SpringBootApplication
public class LaborChartManagerApplication {
	
	@Autowired
	RepoService repoService;

	public static void main(String[] args) {
		SpringApplication.run(LaborChartManagerApplication.class, args);
	}
	
	// Starts bot after registering class
	@EventListener(ApplicationReadyEvent.class)
	public void afterStartup() {
		System.out.println("Butt");
		populateDB();
	}
	
	public void populateDB() {
		// Creates new DCH Position
		repoService.getPositionRepo().save(new Position("DCH", "Dinner Chef", "16:00", "EVERYDAY", 2, 2));
		
		/*
		// Sets blake to DCH Position
		List<LaborSlot> laborSlotList = repoService.getLaborSlotRepo().findByDayOfWeekAndPosition(DayOfWeek.TUESDAY, "DCH");
		LaborSlot updateSlot = laborSlotList.get(0);
		Cooper newCooper = new Cooper("blake", "blake#4416");
		repoService.getMemberRepo().save(newCooper);
		System.out.println(repoService.getMemberRepo().findByUsername("blake"));
		updateSlot.setCooper(newCooper);
		repoService.getLaborSlotRepo().save(updateSlot);
		*/
	}
}
