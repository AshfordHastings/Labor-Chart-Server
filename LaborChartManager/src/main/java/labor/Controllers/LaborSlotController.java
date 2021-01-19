package labor.Controllers;

import java.io.StringReader;
import java.time.DayOfWeek;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.tomcat.util.json.JSONParser;
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import labor.Entity.Cooper;
import labor.Entity.LaborSlot;
import labor.Entity.Position.PositionBuilder;
import labor.Service.RepoService;

@RequestMapping(path="api/laborSlots")
@RestController
public class LaborSlotController {
	
	@Autowired
	RepoService repoService;
	
	@GetMapping(path="/search", params = {"position"})
	public List<LaborSlot> findLaborSlotByPosition(
			@RequestParam(value="position") String position){
		
		return repoService.getLaborSlotRepo().findByPosition(position);
	}
	
	@GetMapping(path="/search", params = {"dayOfWeek", "position"})
	public List<LaborSlot> findLaborSlotByDayOfWeekAndPosition(
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
	
	/*
	@GetMapping(path="/search", params = {"timeSlot"})
	public List<LaborSlot> findLaborSlotByTimeSlot(
			@RequestParam(value="timeSlot") TimeSlot timeSlot){
		return repoService.getLaborSlotRepo().findByTimeSlot(timeSlot);
	}
	*/
	@GetMapping(path="/search", params = {"timeSlot"})
	public List<LaborSlot> findLaborSlotByTimeSlot(
			@RequestParam String timeSlot){
		System.out.println(timeSlot);
		return null;
		//return repoService.getLaborSlotRepo().findByTimeSlot(timeSlot);
	}
	
	@GetMapping(path="/search", params = {"cooperId"})
	public ResponseEntity<List<LaborSlot>> findLaborSlotByCooper(
			@RequestParam(value="cooperId") Long cooperId){
		try {
			Cooper cooperToQuery = repoService.getMemberRepo().findById(cooperId).orElseThrow();
			return new ResponseEntity<List<LaborSlot>>(repoService.getLaborSlotRepo().findByCooper(cooperToQuery), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<LaborSlot> patchLaborSlot(@RequestBody String jsonLaborSlot) {
		try {
			LaborSlot laborSlot = getLaborSlotFromJson(jsonLaborSlot);
			LaborSlot returnSlot = repoService.getLaborSlotRepo().save(laborSlot);
			return new  ResponseEntity<LaborSlot>(returnSlot, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	public LaborSlot getLaborSlotFromJson(String jsonLaborSlot) throws Exception {
		
		System.out.println(jsonLaborSlot);
		
		JsonElement rootNode = JsonParser.parseString(jsonLaborSlot);
		JsonObject details = rootNode.getAsJsonObject();
		LaborSlot oldSlot = repoService.getLaborSlotRepo().findById(details.get("id").getAsString()).orElseThrow();
		
		List<Cooper> queryCooperList = repoService.getMemberRepo().findByDiscordTag(details.get("cooper").getAsJsonObject().get("discordTag").getAsString());
		if(queryCooperList.size() > 0) {
			oldSlot.setCooper(queryCooperList.get(0));
		} else {
			JsonObject jsonCooper = details.get("cooper").getAsJsonObject().getAsJsonObject();
			Cooper cooper = new Cooper(jsonCooper.get("username").getAsString(), jsonCooper.get("discordTag").getAsString());
			oldSlot.setCooper(cooper);
			repoService.getMemberRepo().save(cooper);
		}
		return oldSlot;
	}
}
