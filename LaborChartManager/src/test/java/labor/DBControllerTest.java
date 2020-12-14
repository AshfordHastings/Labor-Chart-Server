package labor;


import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import labor.controllers.DBController;
import labor.data.PositionRepository;
import reactor.core.publisher.Flux;

public class DBControllerTest {

	
	
	
	@Test
	public void getPositionTest1() {
		// Creates mock repository for testing purposes
		Position testPosition1 = new Position("MG", "Morning Grace", "8:00", "EVERYDAY", 1);
		Flux<Position> positionFlux = Flux.just(testPosition1);
		
		PositionRepository positionRepo = Mockito.mock(PositionRepository.class);
		when(positionRepo.findById("MG")).thenReturn(Optional.of(testPosition1));
		
		WebTestClient testClient = WebTestClient.bindToController(
				new DBController(positionRepo)).build();
		
		testClient.get().uri("/database/position/MG")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
				.jsonPath("$").exists()
				.jsonPath("$.id").isEqualTo(testPosition1.getId())
				.jsonPath("$.name").isEqualTo(testPosition1.getName())
				.jsonPath("$.stringTime").isEqualTo(testPosition1.getStringTime())
				.jsonPath("$.length").isEqualTo(testPosition1.getLength());
	}
	
	
}
