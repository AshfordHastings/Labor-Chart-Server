package labor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import labor.data.LaborSlotRepository;
import labor.data.PositionRepository;

@DataJpaTest
public class DBUnitTests {
	
	@Autowired
	PositionRepository positionRepo;
	
	@Autowired
	LaborSlotRepository laborSlotRepo;
	
	@Test
	public void PositionRepoTest1() {
		Position position =new Position("MG", "Morning Grace", "9:00","EVERYDAY", 1, 1);
		
		Position newPosition = positionRepo.save(position);
		newPosition.saveChildren(laborSlotRepo);
		
		
		
		assertThat(positionRepo).isNotNull();
		assertThat(laborSlotRepo).isNotNull();
		
		Optional<Position> optReturnPosition =  positionRepo.findById("MG");
		assertThat(optReturnPosition.isPresent()).isTrue();
		
		Position returnPosition = optReturnPosition.get();
		assertThat(returnPosition.getName()).isEqualTo("Morning Grace");
		
		Iterable<LaborSlot> laborSlotIter = laborSlotRepo.findAll();
		List<LaborSlot> laborSlotList = new ArrayList<LaborSlot>();
		laborSlotIter.forEach(laborSlotList::add);
		assertThat(laborSlotList.size()).isGreaterThan(0);
	}
}
