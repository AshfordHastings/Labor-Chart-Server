package labor.Controllers;

import java.util.ArrayList;
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

import labor.Entity.Cooper;
import labor.Entity.LaborSlot;
import labor.Service.RepoService;

@RequestMapping(path="api/coopers")
@RestController
public class CooperController {
	
	@Autowired
	RepoService repoService;
	
	@GetMapping(path="/search")
	@ResponseStatus(HttpStatus.OK)
	public List<Cooper> findCoopers(
			@RequestParam(value="username", required=false, defaultValue="") String username,
			@RequestParam(value="discordTag", required=false, defaultValue="") String discordTag){
		if(username.isEmpty() && discordTag.isEmpty()) {
		    List<Cooper> result = new ArrayList<Cooper>();
		    repoService.getMemberRepo().findAll().forEach(result::add);
			return result;
		} else if(username.isEmpty()) {
			return repoService.getMemberRepo().findByDiscordTag(discordTag);
		} else if(discordTag.isEmpty()) {
			return repoService.getMemberRepo().findByUsername(username);
		} else {
			return repoService.getMemberRepo().findByUsername(username);
		}
	}
	
	@GetMapping(path="/search", params = {"cooper"})
	public ResponseEntity<List<LaborSlot>> findLaborSlotByCooper(
			@RequestParam(value="cooper") Cooper cooper){
		try {
			System.out.println("Received Cooper for findLaborSlotByCooper - id = " + cooper);
			Cooper cooperToQuery = repoService.getMemberRepo().findById(cooper.getId()).orElseThrow();
			return new ResponseEntity<>(repoService.getLaborSlotRepo().findByCooper(cooperToQuery), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(path="/search", params= {"id"})
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Cooper> findCooperById(
			@RequestParam(value="id") Long id){
		Optional<Cooper> optCooper = repoService.getMemberRepo().findById(id);
		if(optCooper.isPresent()) {
			return new ResponseEntity<Cooper>(optCooper.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cooper postCooper(@RequestBody Cooper cooper) {
		System.out.println("Server has received: " + cooper);
		return repoService.getMemberRepo().save(cooper);
	}
}
