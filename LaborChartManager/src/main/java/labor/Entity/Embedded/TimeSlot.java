package labor.Entity.Embedded;

import java.time.DayOfWeek;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Immutable
@Embeddable
@Data
public class TimeSlot {
	
	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;
	private String timeString;
	
	public TimeSlot() {		
	}
	public TimeSlot(DayOfWeek dayString, String timeString) {
		this.timeString = timeString;
		this.dayOfWeek = dayString;
	}
	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}
	public String getTimeString() {
		return timeString;
	}
	
	
}
