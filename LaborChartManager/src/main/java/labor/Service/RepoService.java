package labor.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import labor.Repositories.LaborSlotRepository;
import labor.Repositories.MemberRepository;
import labor.Repositories.PositionRepository;

@Service
public class RepoService {
	@Autowired
	PositionRepository positionRepo;
	
	@Autowired
	MemberRepository memberRepo;
	
	@Autowired
	LaborSlotRepository laborSlotRepo;
	
	public PositionRepository getPositionRepo() {
		return positionRepo;
	}

	public MemberRepository getMemberRepo() {
		return memberRepo;
	}

	public LaborSlotRepository getLaborSlotRepo() {
		return laborSlotRepo;
	}
	

}
