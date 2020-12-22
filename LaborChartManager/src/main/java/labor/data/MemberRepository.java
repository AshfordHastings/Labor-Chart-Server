package labor.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import labor.Member;

@Repository 
public interface MemberRepository extends CrudRepository<Member, Long>{

}
