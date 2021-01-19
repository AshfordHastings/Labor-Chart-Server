package labor.Entity;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@Table
@Entity
public class Position {
	public static class PositionBuilder {
		private String id;
		private String name;
		private int length;
		private int numSlots;
		private String stringTime;
		private String laborDays;
		
		public Position build() {
			return new Position(this);
		}
		
		public String id() {
			return id;
		}
		public void id(String id) {
			this.id = id;
		}
		public String name() {
			return name;
		}
		public void name(String name) {
			this.name = name;
		}
		public int length() {
			return length;
		}
		public void length(int length) {
			this.length = length;
		}
		public int numSlots() {
			return numSlots;
		}
		public void numSlots(int numSlots) {
			this.numSlots = numSlots;
		}
		public String stringTime() {
			return stringTime;
		}
		public void stringTime(String stringTime) {
			this.stringTime = stringTime;
		}
		public String laborDays() {
			return laborDays;
		}
		public void laborDays(String laborDays) {
			this.laborDays = laborDays;
		}
		
	}
	
	
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
	
	public Position(PositionBuilder builder) {
		this(builder.id(), builder.name(), builder.stringTime(), builder.laborDays(), builder.length(), builder.numSlots());
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
