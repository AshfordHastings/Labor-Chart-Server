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
import labor.Entity.Position;
import labor.Service.RepoService;

@RequestMapping(path="/api/positions")
@RestController
public class PositionController {

	@Autowired
	RepoService repoService;
	
	@GetMapping(path="/coopers/search/findByUsername")
	@ResponseStatus(HttpStatus.OK)
	public List<Cooper> findByUsername(
			@RequestParam(value="username", required=false) String username,
			@RequestParam(value="discordTag", required=false) String discordTag){
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
	
	// Custom post method created to access the Position constructor - which populates LaborSlot entities in the database
	@PostMapping(path="/positions")
	@ResponseStatus(HttpStatus.CREATED)
	public Position savePosition(@RequestBody Position position) {
		Position positionEntity = Position.createPositionFrom(position);
		Position returnPosition = repoService.getPositionRepo().save(positionEntity);
		return returnPosition;
	}
}
