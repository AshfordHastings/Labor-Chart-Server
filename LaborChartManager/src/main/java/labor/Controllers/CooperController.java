package labor.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import labor.Entity.Cooper;
import labor.Service.RepoService;

@RequestMapping(path="api/coopers")
@RestController
public class CooperController {
	
	@Autowired
	RepoService repoService;
	
	@GetMapping(path="/search")
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
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cooper postCooper(@RequestBody Cooper cooper) {
		System.out.println("Server has received: " + cooper);
		return repoService.getMemberRepo().save(cooper);
	}
}
