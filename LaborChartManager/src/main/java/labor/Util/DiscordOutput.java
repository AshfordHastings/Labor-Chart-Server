package labor.Util;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class DiscordOutput {
	MessageChannel messageChannel;
	
	public DiscordOutput(MessageChannel messageChannel) {
		this.messageChannel = messageChannel;
	}
	
	public void sendMessage(String message) {
		messageChannel.sendMessage(message).queue();;
	}
}
