package labor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.Transient;
import labor.configs.LaborConfigs;
import lombok.Data;


@Table(name="Position")
@Data
@Entity
public class Position {
	@Transient
	@Autowired
	LaborConfigs laborConfigs;

	public enum LaborDays {
		WEEKDAYS,
		WEEKENDS,
		EVERYDAY,
		FLEX
	};
	
	@Id
	private String id;
	private String name;
	
	@Column(name="STRINGTIME")
	private String stringTime;
	
	@Column(name="LABORDAYS")
	private String laborDays;
	
	private int length;
	
	@Transient
	private LocalTime localTime;
	@Transient
	private List<DayOfWeek> daysOfWeek;
	
	Position(String id, String name, String stringTime, String laborDays, int length) {
		this.id = id;
		this.name = name;
		this.stringTime = stringTime;
		String[] hourMinute = stringTime.split(":");
		this.localTime = LocalTime.of(Integer.valueOf(hourMinute[0]), Integer.valueOf(hourMinute[1]));
		this.daysOfWeek = getLaborDays(LaborDays.valueOf(laborDays));
		this.length = length;
	}
	
	private List<DayOfWeek> getLaborDays(LaborDays laborDays) {
		List<DayOfWeek> daysOfWeek = new ArrayList<DayOfWeek>();
		
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
			return daysOfWeek;
		}
	
		// TODO: Throw Exception here
		return daysOfWeek;
	}
}
