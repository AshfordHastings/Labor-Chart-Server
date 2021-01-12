package labor.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import labor.Entity.Position;
import labor.Service.RepoService;

@RepositoryRestController
public class PositionController {

	@Autowired
	RepoService repoService;
	
	// Custom post method created to access the Position constructor - which populates LaborSlot entities in the database
	@PostMapping(path="/positions", consumes="application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Position savePosition(@RequestBody Position position) {
		Position positionEntity = Position.createPositionFrom(position);
		return repoService.getPositionRepo().save(positionEntity);
	}
}
