package labor.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import labor.TimeSlot;

@Repository
public interface TimeRepository extends CrudRepository<TimeSlot, String>{

}
