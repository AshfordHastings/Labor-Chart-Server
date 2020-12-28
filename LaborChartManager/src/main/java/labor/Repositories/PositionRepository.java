package labor.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import labor.Entity.Position;

@Repository
public interface PositionRepository extends CrudRepository<Position, String> {
	
}
