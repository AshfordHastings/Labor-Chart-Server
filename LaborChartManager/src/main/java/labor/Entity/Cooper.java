package labor.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cooper {
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private Long id;
	
	private String name;
	private String discordTag;
	
	public Cooper(String name, String discordTag) {
		this.name = name;
		this.discordTag = discordTag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiscordTag() {
		return discordTag;
	}

	public void setDiscordUsername(String discordTag) {
		this.discordTag = discordTag;
	}

	
	
}
