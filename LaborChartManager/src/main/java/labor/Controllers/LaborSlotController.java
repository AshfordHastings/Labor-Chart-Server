package labor.Controllers;

import java.time.DayOfWeek;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import labor.Entity.Cooper;
import labor.Entity.LaborSlot;
import labor.Entity.Embedded.TimeSlot;
import labor.Service.RepoService;

@RequestMapping(path="api/laborSlots")
@RestController
public class LaborSlotController {
	
	@Autowired
	RepoService repoService;
	
	@GetMapping(path="/search", params = {"dayOfWeek", "position"})
	public List<LaborSlot> findLaborSlotByDayOfWeekAndPositionn(
			@RequestParam(value="dayOfWeek") DayOfWeek dayOfWeek,
			@RequestParam(value="position") String position){
		
		return repoService.getLaborSlotRepo().findByDayOfWeekAndPosition(dayOfWeek, position);
	}
	
	@GetMapping(path="/search", params = {"dayOfWeek", "position", "discordTag"})
	public List<LaborSlot> findLaborSlotByDayOfWeekAndPositionAndDiscordTag(
			@RequestParam(value="dayOfWeek") DayOfWeek dayOfWeek,
			@RequestParam(value="position") String position,
			@RequestParam(value="discordTag") String discordTag){
		return repoService.getLaborSlotRepo().findByDayOfWeekAndPositionAndDiscordTag(dayOfWeek, position, discordTag);
	}
	
	@GetMapping(path="/search", params = {"dayOfWeek", "time"})
	public List<LaborSlot> findLaborSlotByDayOfWeekAndTime(
			@RequestParam(value="dayOfWeek") DayOfWeek dayOfWeek,
			@RequestParam(value="time") String time){
		
		return repoService.getLaborSlotRepo().findByDayOfWeekAndTime(dayOfWeek, time);
	}
	
	@GetMapping(path="/search", params = {"timeSlot"})
	public List<LaborSlot> findLaborSlotByTimeSlot(
			@RequestParam(value="timeSlot") TimeSlot timeSlot){
		return repoService.getLaborSlotRepo().findByTimeSlot(timeSlot);
	}
	
	@GetMapping(path="/search", params = {"cooper"})
	public ResponseEntity<List<LaborSlot>> findLaborSlotByCooper(
			@RequestParam(value="cooper") Long cooper){
		try {
			System.out.println("Received Cooper for findLaborSlotByCooper - id = " + cooper);
			Cooper cooperToQuery = repoService.getMemberRepo().findById(cooper).orElseThrow();
			return new ResponseEntity<>(repoService.getLaborSlotRepo().findByCooper(cooperToQuery), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@PatchMapping
	@ResponseStatus(HttpStatus.OK)
	public LaborSlot patchLaborSlot(@RequestParam LaborSlot laborSlot) {
		return repoService.getLaborSlotRepo().save(laborSlot);
	}
}
