package labor.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.dv8tion.jda.api.entities.Member;

@Entity
public class Cooper {
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private Long id;
	private String username;
	private String discordTag;
	
	public Cooper(Member member) {
		this.username = member.getUser().getAsTag();
		this.discordTag = member.getAsMention();
	}
	public Cooper(String username, String discordTag) {
		this.username = username;
		this.discordTag = discordTag;
	}
	
	Cooper() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public String getDiscordTag() {
		return discordTag;
	}

	public void setDiscordUsername(String discordTag) {
		this.discordTag = discordTag;
	}
	@Override
	public String toString() {
		return "Cooper [id=" + id + ", username=" + username + ", discordTag=" + discordTag + "]";
	}

	
	
	
}
