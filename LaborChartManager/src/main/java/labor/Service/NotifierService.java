package labor.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import labor.Notifier.LaborScheduler;

@Service
public class NotifierService{
	
	@Autowired
	LaborService laborService;
	
	public void setNotifyTime(DayOfWeek dayOfWeek, LocalTime time) {
		if(!(laborService.getRepoService().getLaborSlotRepo().findByDayOfWeekAndTime(dayOfWeek, time.toString()).size() > 0)) {
		
			ThreadPoolTaskScheduler laborScheduler = new ThreadPoolTaskScheduler();
			laborScheduler
				.initialize();
			laborScheduler.schedule(
					new LaborScheduler(dayOfWeek, time.toString(), laborService),
					new CronTrigger("0 " + time.getMinute() + " " + time.getHour() + " * * " + dayOfWeek.getValue()));
			//new CronTrigger("* * " + time.getHour() + " * * " + dayOfWeek.getValue() + " * "));
			}
	}
}
