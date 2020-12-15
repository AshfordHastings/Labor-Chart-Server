package labor;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import labor.data.LaborSlotRepository;
import labor.data.PositionRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {"spring.datasource.data=classpath:data-positions.sql"})
public class PositionRepoTest {
	@Autowired
	PositionRepository positionRepo;
	
	@Autowired
	LaborSlotRepository laborSlotRepo;
	
	@Test
	public void positionRepoTest1() {
		assertThat(positionRepo).isNotNull();
		Optional<Position> optPosition = positionRepo.findById("MG");
		assertThat(optPosition).isNotNull();
		Position position = optPosition.get();
		assertThat(position.getName()).isEqualTo("Morning Grace");
		assertThat(position.getLength()).isEqualTo(1);
	}
	
	@Test
	public void laborSlotRepoTest1() {
		Optional<Position> optionalPosition = positionRepo.findById("MG");
		assertThat(optionalPosition.isPresent()).isTrue();
		Position position = optionalPosition.get();
		
		LaborSlot returnSlot = laborSlotRepo.save(new LaborSlot(position));
		assertThat(laborSlotRepo).isNotNull();
		
		Optional<LaborSlot> optLaborSlot = laborSlotRepo.findById(returnSlot.getId());
		assertThat(optLaborSlot.isPresent()).isTrue();
		
		LaborSlot laborSlot = optLaborSlot.get();
		assertThat(laborSlot.getPosition()).isEqualTo(position);
	}
	
	
}
