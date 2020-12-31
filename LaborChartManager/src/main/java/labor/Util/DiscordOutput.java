package labor.Util;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class DiscordOutput {
	MessageChannel messageChannel;
	
	public DiscordOutput(MessageChannel defaultChannel) {
		this.messageChannel = defaultChannel;
	}
	
	public void sendMessage(String message) {
		messageChannel.sendMessage(message).queue();
	}
}
