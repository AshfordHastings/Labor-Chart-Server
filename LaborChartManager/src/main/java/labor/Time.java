package labor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.Data;


public class Time {
	@Id
	String timeDayString;
	
	@Transient
	DayOfWeek dayOfWeek;
	@Transient
	LocalTime localTime;
	
	@OneToMany
	private List<LaborSlot> laborSlots;
	
	Time(String day, String time) {
		timeDayString = day.concat(":").concat(time);
	}
}
