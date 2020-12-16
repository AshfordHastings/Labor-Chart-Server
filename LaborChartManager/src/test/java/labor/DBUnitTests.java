package labor;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import labor.data.LaborSlotRepository;
import labor.data.PositionRepository;
import labor.data.TimeRepository;

@DataJpaTest
public class DBUnitTests {
	
	@Autowired
	PositionRepository positionRepo;
	
	@Autowired
	LaborSlotRepository laborSlotRepo;
	
	@Autowired
	TimeRepository timeRepo;
	
	@Test
	public void PositionRepoTest1() {
		// Creates and stores new Position in the database, creates LaborSlot Children
		Position position =new Position("MG", "Morning Grace", "9:00","EVERYDAY", 1, 1);
		Position newPosition = positionRepo.save(position);
		newPosition.saveChildren(laborSlotRepo, timeRepo);
		
		// Tests if Position and LaborSlots were properly stored in the database
		assertThat(positionRepo).isNotNull();
		assertThat(laborSlotRepo).isNotNull();
		assertThat(timeRepo).isNotNull();
		
		// Is "Morning Grace" stored in Position Repository
		Optional<Position> optReturnPosition =  positionRepo.findById("MG");
		assertThat(optReturnPosition.isPresent()).isTrue();
		Position returnPosition = optReturnPosition.get();
		assertThat(returnPosition.getName()).isEqualTo("Morning Grace");
		
		// Did saving "Morning Grace" in the position Repository create the correct number of Labor Slots?
		Iterable<LaborSlot> laborSlotIter = laborSlotRepo.findAll();
		Map<String, LaborSlot> laborSlotList = new HashMap<String, LaborSlot>();
		for(LaborSlot laborSlot : laborSlotIter) {
			laborSlotList.put(laborSlot.getId(), laborSlot);
		}
		assertThat(laborSlotList.size()).isEqualTo(7 * newPosition.getNumSlots()); // Asserts that a LaborSlot of Position was created for each day of the week
		
		LaborSlot testSlot = laborSlotList.get("TUESDAY:9:00:MG:0");
		assertThat(testSlot).isNotNull(); // asserts that getTestSlot() from LaborSlotRepo isn't null
		TimeSlot testTime = testSlot.getTime(); 
		assertThat(testTime).isNotNull(); // asserts that testSlot has non-null Time mapping
		assertThat(testTime.getTimeSlotString()).isEqualTo("TUESDAY:9:00"); // asserts that the testSlot's Time has a proper Id
		assertThat(testTime.getLaborSlots().size()).isEqualTo(1); // asserts that proper number of Labor Slots are stored in each Time
		
		Optional<TimeSlot> dbOptTime = timeRepo.findById("TUESDAY:9:00"); // Gets Time Directly from TimeRepo  
		assertThat(dbOptTime).isPresent(); // asserts that findById from TimeRepo returns a non-null Time
		TimeSlot dbTime = dbOptTime.get();
		assertThat(dbTime.getLaborSlots()).contains(testSlot); // Assert that the Time object contains testSlot in a list

		
		
		
		
	}
}
