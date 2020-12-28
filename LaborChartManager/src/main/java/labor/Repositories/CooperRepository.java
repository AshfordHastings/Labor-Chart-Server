package labor.Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import labor.Entity.Cooper;

@Repository 
public interface CooperRepository extends CrudRepository<Cooper, Long>{
	List<Cooper> findByDiscordTag(String discordTag);
}
