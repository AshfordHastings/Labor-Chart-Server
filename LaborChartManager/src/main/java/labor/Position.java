package labor;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import labor.data.LaborSlotRepository;
import labor.data.TimeRepository;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table
@NoArgsConstructor
@Data
@Entity
public class Position {
	public enum LaborDays {
		WEEKDAYS,
		WEEKENDS,
		EVERYDAY,
		FLEX
	};
	
	@Id
	private String id;
	private String name;
	private int length;
	private int numSlots;
	
	@ElementCollection
	@Column(name = "daysOfWeek")
	private Set<DayOfWeek> daysOfWeek;
	
	@Column(name="STRINGTIME")
	private String stringTime;
	

	Position(String id, String name, String stringTime, String laborDays, int length, int numSlots) {
		this.id = id;
		this.name = name;
		this.numSlots = numSlots;
		this.length = length;
		setDaysOfWeek(laborDays);
		
		this.stringTime = stringTime;
		//String[] hourMinute = stringTime.split(":");
		//this.localTime = LocalTime.of(Integer.valueOf(hourMinute[0]), Integer.valueOf(hourMinute[1]));

	}

	public void saveChildren(LaborSlotRepository laborSlotRepo, TimeRepository timeRepo) {
		for(DayOfWeek day : daysOfWeek) {
			for(int numSlot = 0; numSlot < numSlots; numSlot++) {
				LaborSlot newSlot = new LaborSlot(this, day, stringTime, numSlot);
				newSlot.saveChildren(timeRepo);
				laborSlotRepo.save(newSlot);
			}
		}
	}
	
	private void setDaysOfWeek(String laborString) {
		LaborDays laborDays = LaborDays.valueOf(laborString);
		daysOfWeek = new HashSet<DayOfWeek>();
		
		if(laborDays == LaborDays.WEEKDAYS || laborDays == LaborDays.EVERYDAY) {
			daysOfWeek.add(DayOfWeek.MONDAY);
			daysOfWeek.add(DayOfWeek.TUESDAY);
			daysOfWeek.add(DayOfWeek.WEDNESDAY);
			daysOfWeek.add(DayOfWeek.THURSDAY);
			daysOfWeek.add(DayOfWeek.FRIDAY);	
		}
		
		if(laborDays == LaborDays.WEEKENDS || laborDays == LaborDays.EVERYDAY) {
			daysOfWeek.add(DayOfWeek.SATURDAY);
			daysOfWeek.add(DayOfWeek.SUNDAY);
		}
		
		if(laborDays == LaborDays.FLEX) {
			daysOfWeek = null;
		}
	
	}
}
