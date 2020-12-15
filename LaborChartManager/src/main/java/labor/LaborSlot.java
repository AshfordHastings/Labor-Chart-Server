package labor;

import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="labor_chart")
@NoArgsConstructor
@Data
public class LaborSlot {
	LaborSlot(Position position, DayOfWeek laborDay, LocalTime laborTime) {
		this.position = position;
		this.laborDay = laborDay;
		this.laborTime = laborTime;
		id = new String(laborDay.toString()) + ":" + laborTime.toString();
	}
	
	// Day:Hour:Minute
	@Id
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "position_id")
	private Position position;
	
	private DayOfWeek laborDay;
	private LocalTime laborTime;
}
