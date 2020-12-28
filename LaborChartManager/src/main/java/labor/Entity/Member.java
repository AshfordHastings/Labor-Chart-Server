package labor.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member {
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private Long id;
	
	private String name;
	private String discordUsername;
	private int discordNum;
	
	Member(String name, String discordUsername, int discordNum) {
		this.name = name;
		this.discordUsername = discordUsername;
		this.discordNum = discordNum;
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

	public String getDiscordUsername() {
		return discordUsername;
	}

	public void setDiscordUsername(String discordUsername) {
		this.discordUsername = discordUsername;
	}

	public int getDiscordNum() {
		return discordNum;
	}

	public void setDiscordNum(int discordNum) {
		this.discordNum = discordNum;
	}
	
	
}
