package labor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import labor.configs.LaborConfigs;
import labor.data.LaborSlotRepository;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table
@NoArgsConstructor
@Data
@Entity
public class Position {
	@Transient
	@Autowired
	LaborConfigs laborConfigs;
	
	@Transient
	@Autowired
	LaborSlotRepository laborSlotRepo;

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
	
	private LocalTime localTime;

	Position(String id, String name, String stringTime, String laborDays, int length, int numSlots) {
		this.id = id;
		this.name = name;
		this.numSlots = numSlots;
		this.length = length;
		setDaysOfWeek(laborDays);
		
		this.stringTime = stringTime;
		String[] hourMinute = stringTime.split(":");
		this.localTime = LocalTime.of(Integer.valueOf(hourMinute[0]), Integer.valueOf(hourMinute[1]));

	}

	public void saveChildren(LaborSlotRepository laborSlotRepo) {
		for(DayOfWeek day : daysOfWeek) {
			laborSlotRepo.save(new LaborSlot(this, day, localTime));
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
