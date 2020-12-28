package labor.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import labor.Entity.Cooper;

@Repository 
public interface CooperRepository extends CrudRepository<Cooper, Long>{

}
