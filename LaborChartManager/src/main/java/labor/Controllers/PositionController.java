package labor.Controllers;

import java.io.StringReader;
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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import labor.Entity.LaborSlot;
import labor.Entity.Position;
import labor.Entity.Position.PositionBuilder;
import labor.Service.RepoService;

@RequestMapping(path = "/api/positions")
@RestController
public class PositionController {

	@Autowired
	RepoService repoService;

	@GetMapping(path = "/search", params = { "dayOfWeek", "time" })
	public List<LaborSlot> findLaborSlots(@RequestParam(value = "dayOfWeek") DayOfWeek dayOfWeek,
			@RequestParam(value = "time") String time) {

		return repoService.getLaborSlotRepo().findByDayOfWeekAndTime(dayOfWeek, time);
	}

	@GetMapping(path = "/search", params = { "id" })
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Position> findPositionById(@RequestParam(value = "id") String id) {
		try {
			Optional<Position> positionQuery = repoService.getPositionRepo().findById(id);
			return new ResponseEntity<>(positionQuery.orElseThrow(), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	// Custom post method created to access the Position constructor - which
	// populates LaborSlot entities in the database
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Position> postPosition(@RequestBody String jsonPosition) {
		try { 
			System.out.println("Server has received: " + jsonPosition);
			Position position = getPositionFromJson(jsonPosition);
			Position returnPosition = repoService.getPositionRepo().save(position);
			return new ResponseEntity<>(returnPosition, HttpStatus.ACCEPTED);
			
		} catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	public Position getPositionFromJson(String jsonPosition) throws Exception {
		PositionBuilder positionBuilder = new PositionBuilder();
		JsonReader reader = new JsonReader(new StringReader(jsonPosition));
		String fieldName = null;

		reader.beginObject();

		fieldName = reader.nextName();
		if (fieldName.equals("id")) {
			positionBuilder.id(reader.nextString());
		} else {
			throw new Exception();
		}

		fieldName = reader.nextName();
		if (fieldName.equals("name")) {
			positionBuilder.name(reader.nextString());
		} else {
			throw new Exception();
		}

		fieldName = reader.nextName();
		if (fieldName.equals("length")) {
			positionBuilder.length(reader.nextInt());
		} else {
			throw new Exception();
		}

		fieldName = reader.nextName();
		if (fieldName.equals("numSlots")) {
			positionBuilder.numSlots(reader.nextInt());
		} else {
			throw new Exception();
		}

		fieldName = reader.nextName();
		if (fieldName.equals("stringTime")) {
			positionBuilder.stringTime(reader.nextString());
		} else {
			throw new Exception();
		}

		fieldName = reader.nextName();
		if (fieldName.equals("laborDays")) {
			positionBuilder.laborDays(reader.nextString());
		} else {
			throw new Exception();
		}
		
		reader.close();
		return positionBuilder.build();

	}

}
