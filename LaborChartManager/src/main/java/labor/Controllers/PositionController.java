package labor.Controllers;

import java.time.DayOfWeek;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import labor.Entity.LaborSlot;
import labor.Entity.Position;
import labor.Service.RepoService;

@RequestMapping(path="/api/positions")
@RestController
public class PositionController {

	@Autowired
	RepoService repoService;
	
	@GetMapping(path="/search", params = {"dayOfWeek", "time"})
	public List<LaborSlot> findLaborSlots(
			@RequestParam(value="dayOfWeek") DayOfWeek dayOfWeek,
			@RequestParam(value="time") String time){
		
		return repoService.getLaborSlotRepo().findByDayOfWeekAndTime(dayOfWeek, time);
	}
	
	@GetMapping(path="/search", params = {"id"})
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Position> findPositionById(
			@RequestParam(value="id") String id) {
		try {
			Optional<Position> positionQuery = repoService.getPositionRepo().findById(id);
			return new ResponseEntity<>(positionQuery.orElseThrow(), HttpStatus.OK);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	
	// Custom post method created to access the Position constructor - which populates LaborSlot entities in the database
	@PostMapping(path="/positions")
	@ResponseStatus(HttpStatus.CREATED)
	public Position savePosition(@RequestBody Position position) {
		Position positionEntity = Position.createPositionFrom(position);
		Position returnPosition = repoService.getPositionRepo().save(positionEntity);
		return returnPosition;
	}
}
