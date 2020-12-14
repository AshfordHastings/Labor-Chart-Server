package labor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;


public class Time {
	@Id
	String id;
	
	DayOfWeek dayOfWeek; 	
	LocalTime localTime;
	
	@OneToMany
	private List<LaborSlot> laborSlots;
}
