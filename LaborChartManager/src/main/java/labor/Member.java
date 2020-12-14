package labor;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
public class Member {
	
	@Id
	private Long id;
	
	
	@ManyToMany
	private List<LaborSlot> laborSlot;
	
}
