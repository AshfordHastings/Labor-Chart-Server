package labor;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import labor.Entity.LaborSlot;
import labor.Entity.Member;
import labor.Entity.Position;
import labor.Entity.Embedded.TimeSlot;
import labor.Repositories.LaborSlotRepository;
import labor.Repositories.MemberRepository;
import labor.Repositories.PositionRepository;

@DataJpaTest
public class DBUnitTests {
	
	@Autowired
	PositionRepository positionRepo;
	
	@Autowired
	LaborSlotRepository laborSlotRepo;
	
	@Autowired
	MemberRepository memberRepo;
	
	
	// @Test
	public void PositionRepoTest1() {
		// Creates and stores new Position in the database, creates LaborSlot Children
		Position position =new Position("MG", "Morning Grace", "9:00","EVERYDAY", 1, 1);
		Position newPosition = positionRepo.save(position);
		
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
	
	@Test
	public void RepoTest2() {
		// Creates and stores new Position in the database, creates LaborSlot Children
		Position position =new Position("MG", "Morning Grace", "9:00","EVERYDAY", 1, 1);
		Position newPosition = positionRepo.save(position);
		
		// Tests if Position and LaborSlots were properly stored in the database
		assertThat(positionRepo).isNotNull();
		assertThat(laborSlotRepo).isNotNull();
		
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
		
		// Test creation of embedded TimeSlot in a LaborSlot
		LaborSlot testSlot = laborSlotList.get("TUESDAY:9:00:MG:0");
		assertThat(testSlot).isNotNull(); // asserts that getTestSlot() from LaborSlotRepo isn't null
		TimeSlot testTime = testSlot.getTime(); 
		assertThat(testTime).isNotNull(); // asserts that testSlot has non-null Time mapping
		assertThat(testTime.getDayOfWeek().toString()).isEqualTo("TUESDAY");
		assertThat(testTime.getTimeString()).isEqualTo("9:00");
		
		// Creates a new Member object
		Member testMember = memberRepo.save(new Member("Keoni", "crowley", 9782));
		assertThat(memberRepo).isNotNull();
		
		// Tests findById using LaborSlot
		Optional<LaborSlot> optLaborSlot = laborSlotRepo.findById("TUESDAY:9:00:MG:0");
		assertThat(optLaborSlot.isPresent()).isTrue();
		LaborSlot testLaborSlot = optLaborSlot.get();
		assertThat(testLaborSlot.getPosition().getName()).isEqualTo("Morning Grace");
		
		// Saves the Member to the position
		testLaborSlot.setMember(testMember);
		LaborSlot returnSlot = laborSlotRepo.save(testLaborSlot);
		assert(returnSlot.getMember()).equals(testMember);	
	}
	
	@Test
	public void RepoTest3() {
		Position testPosition = positionRepo.save(new Position("MG", "Morning Grace", "9:00","EVERYDAY", 1, 1));
		Member testMember = memberRepo.save(new Member("Keoni", "crowley", 9782));
		
		List<LaborSlot> queryReturn= laborSlotRepo.findByTimeSlotAndPosition(new TimeSlot(DayOfWeek.TUESDAY, "9:00"), "MG");
		LaborSlot morningGraceTuesday = queryReturn.get(0);
		
		assertThat(queryReturn.size()).isEqualTo(1);
		assertThat(morningGraceTuesday.getId()).isEqualTo("TUESDAY:9:00:MG:0");
		
		morningGraceTuesday.setMember(testMember);
		laborSlotRepo.save(morningGraceTuesday);
		
		List<LaborSlot> tuesdayMorningLaborSlots = laborSlotRepo.findByTimeSlot(new TimeSlot(DayOfWeek.TUESDAY, "9:00"));
		assertThat(tuesdayMorningLaborSlots.size()).isEqualTo(1);
		Member morningGraceMember = tuesdayMorningLaborSlots.get(0).getMember();
		assertThat(morningGraceMember.getName()).isEqualTo("Keoni");
		
		//List<LaborSlot>  = laborSlotRepo.findByTimeSlot(new TimeSlot(DayOfWeek.MONDAY, "9:00"));
	}
}
