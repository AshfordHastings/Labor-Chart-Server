package labor.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import labor.Command.CommandService;

@Service
public class LaborService {

	@Autowired
	JDAService jdaService;
	
	@Autowired
	RepoService repoService;
	
	@Autowired
	CommandService commandService;
	
	@Autowired
	NotifierService notifierService;

	public JDAService getJdaService() {
		return jdaService;
	}

	public RepoService getRepoService() {
		return repoService;
	}

	public CommandService getCommandService() {
		return commandService;
	}
	
	public NotifierService getNotifierService() {
		return notifierService;
	}
	
	
}
