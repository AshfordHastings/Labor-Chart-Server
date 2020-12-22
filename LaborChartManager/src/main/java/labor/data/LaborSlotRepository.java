package labor.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import labor.LaborSlot;
import labor.TimeSlot;


public interface LaborSlotRepository extends CrudRepository<LaborSlot, String>{
	List<LaborSlot> findByTimeSlot(TimeSlot timeSlot);
	
	@Query("from LaborSlot laborSlot where laborSlot.timeSlot=:timeSlot and laborSlot.position.id=:positionId")
	List<LaborSlot> findByTimeSlotAndPosition(@Param("timeSlot") TimeSlot timeSlot, @Param("positionId") String positionId);
}
