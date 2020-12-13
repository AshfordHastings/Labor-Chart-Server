package labor.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import labor.Position;

@Repository
public interface PositionRepository extends CrudRepository<Position, String> {

}
