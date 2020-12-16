package labor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.persistence.CascadeType;

import labor.data.TimeRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="labor_chart")
public class LaborSlot {
	LaborSlot() {
		
	}
	
	LaborSlot(Position position, DayOfWeek laborDay, String stringTime, int positionNumber) {
		this.position = position;
		this.laborDay = laborDay;
		setTime(stringTime);
		setLaborSlotId(laborDay.toString(), stringTime, positionNumber);
		System.out.println("Creating LaborSlot with Id: " + this.id);
	}

	// Day:Hour:Minute
	@Id
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "position_id")
	private Position position;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private TimeSlot timeSlot;
	private String stringTime;
	
	private DayOfWeek laborDay;
	
	public void setTime(String stringTime) {
		this.stringTime = stringTime;
		//String[] hourMinute = stringTime.split(":");
		//timeSeconds = LocalTime.of(Integer.valueOf(hourMinute[0]), Integer.valueOf(hourMinute[1])).toSecondOfDay();
	}
	
	public void saveChildren(TimeRepository timeRepo) {
		// TODO: Add a custom method in the timeRepo interface
		Optional<TimeSlot> optTime = timeRepo.findById(new String(this.laborDay.toString() + ":" + this.stringTime));
		
		if(optTime.isPresent()) {
			TimeSlot timeSlot = optTime.get();
			timeSlot.addLaborSlot(this);
			this.timeSlot = timeSlot;
			timeRepo.save(timeSlot);
		} else {
			this.timeSlot = new TimeSlot(laborDay.toString(), this.stringTime);
			timeRepo.save(this.timeSlot);
			this.timeSlot.addLaborSlot(this);
		}
	}
	
	public String setLaborSlotId(String stringDay, String stringTime, int positionNumber) {
		String myId = new String(stringDay + ":" + stringTime + ":" + position.getId() + ":" + positionNumber);
		this.id = myId;
		return myId;
	}
	
	@Override
	public String toString() {
		return new String(this.id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public TimeSlot getTime() {
		return timeSlot;
	}

	public void setTime(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}


	public String getStringTime() {
		return stringTime;
	}

	public void setStringTime(String stringTime) {
		this.stringTime = stringTime;
	}

	public DayOfWeek getLaborDay() {
		return laborDay;
	}

	public void setLaborDay(DayOfWeek laborDay) {
		this.laborDay = laborDay;
	}
	
	
	
}
