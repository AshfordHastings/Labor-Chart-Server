package labor.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;

public class BotConfiguration {
	
	@Value("${token}")
	private String token;
	
	@Bean
	public GatewayDiscordClient gatewayDiscordClient() {
		GatewayDiscordClient client = DiscordClientBuilder.create(token)
				.build()
				.login()
				.block();
		
		client.getEventDispatcher()
			.on(ReadyEvent.class)
			.subscribe(event -> {
				User self = event.getSelf();
				System.out.println("Logged in as: " + self.getUsername());
			});
		client.onDisconnect().block();
		return client;
	}

}
