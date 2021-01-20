package labor.Controllers;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import antlr.debug.Event;
import labor.Entity.LaborSlot;
import labor.Service.RepoService;

@RestController
@RequestMapping("/sse")
public class NotifierController {
	@Autowired
	RepoService repoService;

	private SseEmitter emitter;

	@GetMapping(path = "/laborUpdates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	SseEmitter createConnection() {
		emitter = new SseEmitter();
		return emitter;
	}

	private final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

	@PostConstruct
    void init() {
        scheduledThreadPool.scheduleAtFixedRate(() -> {
    		LocalTime currentTime = LocalTime.now();
    		Calendar calendar = Calendar.getInstance();
    		DayOfWeek day = DayOfWeek.of(calendar.get(Calendar.DAY_OF_WEEK));
    		
    		try { 
    		List<LaborSlot> laborSlotQuery = repoService.getLaborSlotRepo().findByDayOfWeekAndTime(day, new String(currentTime.getHour() + ":" + currentTime.getMinute()));
			for(LaborSlot laborSlot : laborSlotQuery) {
				emitter.send(laborSlot);
			}
    		} catch(Exception e) {
    			System.out.println("Error sending over emitter.");
    		}
        },0,1,TimeUnit.MINUTES);

	}

	// in another thread
	void sendEvents() {
		try {
			emitter.send("Alpha");
			emitter.send("Omega");

			emitter.complete();
		} catch (Exception e) {
			emitter.completeWithError(e);
		}
	}
}
