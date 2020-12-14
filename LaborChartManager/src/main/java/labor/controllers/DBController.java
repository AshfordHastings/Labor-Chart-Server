package labor.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import labor.Member;
import labor.Position;
import labor.data.PositionRepository;

@RestController
@RequestMapping(path="/database", produces={"application/json", "text/xml"})
@CrossOrigin(origins="*")
public class DBController {
	@Autowired(required=false)
	PositionRepository positionRepo;
	
	
	@GetMapping("position/{positionId}")
	public ResponseEntity<Position> getPositionById(@PathVariable("positionId") String id) {
		Optional<Position> optionalPosition = positionRepo.findById(id);
		if(optionalPosition.isPresent()) {
			return new ResponseEntity<>(optionalPosition.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(consumes="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Member createMember(@RequestBody Member member) {
		return new Member();
	
	}
	
	public DBController(PositionRepository positionRepo) {
		this.positionRepo = positionRepo;
	}
	
	public void setPositionRepo(PositionRepository positionRepo) {
		this.positionRepo = positionRepo;
	}
	
	
}
