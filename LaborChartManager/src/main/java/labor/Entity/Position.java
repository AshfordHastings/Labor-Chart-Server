package labor.Entity;

import java.time.DayOfWeek;
import java.time.LocalTime;
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

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Table
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
	private String laborDays;
	private String apple;
	
	@ElementCollection
	@Column(name = "daysOfWeek")
	private Set<DayOfWeek> daysOfWeek = new HashSet<DayOfWeek>();
	
	@OneToMany(mappedBy = "position",
				fetch = FetchType.LAZY,
				cascade = {CascadeType.ALL
						}
				)
	private Set<LaborSlot> laborSlots = new HashSet<>();

	public Position(String id, String name, String stringTime, String laborDays, int length, int numSlots) {
		this.id = id;
		this.name = name;
		this.numSlots = numSlots;
		this.length = length;
		this.stringTime = stringTime;
		this.laborDays = laborDays;
		setDaysOfWeek(laborDays.toUpperCase());
		mapTimeSlots();

	}
	
	// creates a new Position Entity that properly populates the DaysOfWeek and creates laborSlots in the constructor
	static public Position createPositionFrom(Position position) {
		return new Position(position.getId(), position.getName(), position.getStringTime(), position.getLaborDays(), position.getLength(), position.getNumSlots());
	}

	Position() {
	}
	
	public void mapTimeSlots() {
		for(DayOfWeek day : daysOfWeek) {
			for(int numSlot = 0; numSlot < numSlots; numSlot++) {
				laborSlots.add(new LaborSlot(this, day, stringTime, numSlot));
				
				//TODO: Find way to map events - maybe from response to position creation?
				//laborService.getNotifierService().setNotifyTime(day, LocalTime.parse(stringTime));
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
	
	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		StringBuilder positionString = new StringBuilder();
		positionString.append("Position: " + '\n' +
								'\t' + "Id: " + id + '\n' +
								'\t' + "Name: " + name + '\n' +
								'\t' + "Time: " + stringTime + '\n' +
								'\t' + "Frequency: " + laborDays + '\n' +
								'\t' + "Length: " + length + " hours" + '\n' +
								'\t' + "Number of Slots: " + numSlots + '\n');
		return positionString.toString();
	}
	
	public String getName() {
		return name;
	}
	
	public String getStringTime() {
		return stringTime;
	}

	public int getLength() {
		return length;
	}

	public int getNumSlots() {
		return numSlots;
	}

	public String getLaborDays() {
		return laborDays;
	}
	
	
}
