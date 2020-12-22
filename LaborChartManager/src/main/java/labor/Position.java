package labor;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import labor.data.LaborSlotRepository;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table
@NoArgsConstructor
@Data
@Entity
public class Position {
	public static enum LaborDays {
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
	private String stringTime;
	
	@ElementCollection
	@Column(name = "daysOfWeek")
	private Set<DayOfWeek> daysOfWeek;
	
	@OneToMany(mappedBy = "position",
				fetch = FetchType.LAZY,
				cascade = {CascadeType.ALL
						}
				)
	private Set<LaborSlot> laborSlots = new HashSet<>();

	Position(String id, String name, String stringTime, String laborDays, int length, int numSlots) {
		this.id = id;
		this.name = name;
		this.numSlots = numSlots;
		this.length = length;
		this.stringTime = stringTime;
		setDaysOfWeek(laborDays);
	
		mapTimeSlots();

	}

	
	
	public void mapTimeSlots() {
		for(DayOfWeek day : daysOfWeek) {
			for(int numSlot = 0; numSlot < numSlots; numSlot++) {
				laborSlots.add(new LaborSlot(this, day, stringTime, numSlot));
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
