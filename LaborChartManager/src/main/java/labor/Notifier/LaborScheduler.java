package labor.Notifier;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import labor.Entity.LaborSlot;
import labor.Service.LaborService;

public class LaborScheduler implements Runnable{
	
	LaborService laborService;
	
	DayOfWeek dayOfWeek;
	String timeString;
	
	LaborScheduler() {
		
	}
	
	public LaborScheduler(DayOfWeek dayOfWeek, String timeString, LaborService laborService) {
		this.dayOfWeek = dayOfWeek;
		this.timeString = timeString;
		this.laborService = laborService;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		List<LaborSlot> laborSlotQueryReturn = laborService.getRepoService().getLaborSlotRepo().findByDayOfWeekAndTime(dayOfWeek, timeString);
		System.out.println("Query size: " + laborSlotQueryReturn.size());
		
		if(!(laborSlotQueryReturn.size() > 0)) {
			return;
		} else {
			StringBuilder returnString  = new StringBuilder();
			for(LaborSlot laborSlot : laborSlotQueryReturn) {
				if(laborSlot.getMember() != null) {
					returnString.append(laborSlot.getMember().getDiscordTag() + " has " + laborSlot.getPosition().getName() + " at " + laborSlot.getTime().getTimeString() + "!\n");
				}
			}
			laborService.getJdaService().getDefaultOutput().sendMessage(returnString.toString());
		}
	}
		
		
		
}
