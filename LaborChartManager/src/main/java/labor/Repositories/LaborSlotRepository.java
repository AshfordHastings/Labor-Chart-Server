package labor.Repositories;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import labor.Entity.Cooper;
import labor.Entity.LaborSlot;
import labor.Entity.Embedded.TimeSlot;


public interface LaborSlotRepository extends CrudRepository<LaborSlot, String>{
	List<LaborSlot> findByTimeSlot(TimeSlot timeSlot);
	
	@Query("from LaborSlot laborSlot where (laborSlot.position.id=:position OR laborSlot.position.name=:position)")
	List<LaborSlot> findByPosition(@Param("position") String position);
	
	@Query("from LaborSlot laborSlot where laborSlot.timeSlot.dayOfWeek=:dayOfWeek and (laborSlot.position.id=:position OR laborSlot.position.name=:position)")
	List<LaborSlot> findByDayOfWeekAndPosition(@Param("dayOfWeek") DayOfWeek dayOfWeek, @Param("position") String position);
	
	@Query("from LaborSlot laborSlot where laborSlot.timeSlot.dayOfWeek=:dayOfWeek and (laborSlot.position.id=:position OR laborSlot.position.name=:position) and laborSlot.cooper.discordTag=:discordTag")
	List<LaborSlot> findByDayOfWeekAndPositionAndDiscordTag(@Param("dayOfWeek") DayOfWeek dayOfWeek, @Param("position") String position, @Param("discordTag")String discordTag);

	@Query("from LaborSlot laborSlot where laborSlot.timeSlot.dayOfWeek=:dayOfWeek and laborSlot.timeSlot.timeString=:timeString")
	List<LaborSlot> findByDayOfWeekAndTime(@Param("dayOfWeek") DayOfWeek dayOfWeek, @Param("timeString") String time);
	
	List<LaborSlot> findByCooper(Cooper cooper);

}
