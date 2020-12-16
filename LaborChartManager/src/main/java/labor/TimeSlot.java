package labor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;import org.hibernate.annotations.CascadeType;

import lombok.Data;

@Entity
public class TimeSlot {
	@Id
	String timeSlotString;
	
	@OneToMany
	private List<LaborSlot> laborSlots = new ArrayList<LaborSlot>();
	
	private String timeString;
	private String dayString;
	
	public TimeSlot() {		
	}
	public TimeSlot(String dayString, String timeString) {
		this.timeString = timeString;
		this.dayString = dayString;
		this.timeSlotString = new String(dayString + ":" + timeString);
	}

	public void addLaborSlot(LaborSlot laborSlot) {
		laborSlots.add(laborSlot);
	}

	public String getTimeSlotString() {
		return timeSlotString;
	}
	
	public List<LaborSlot> getLaborSlots() {
		return laborSlots;
	}
}
