package labor;


import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import labor.Controllers.DBController;
import labor.Entity.Position;
import labor.Repositories.PositionRepository;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
public class DBControllerTest {
	@Mock
	PositionRepository positionRepo;
	
	@InjectMocks
	DBController dbController;
	
	@Before
	public void init() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void getPositionTest1() {
		Position testPosition1 = new Position("MG", "Morning Grace", "8:00", "EVERYDAY", 1);
		//PositionRepository positionRepo = Mockito.mock(PositionRepository.class);
		
		Mockito.when(positionRepo.findById(testPosition1.getId())).thenReturn(Optional.of(testPosition1));
		WebTestClient testClient = WebTestClient.bindToController(
				dbController).build();
		
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
