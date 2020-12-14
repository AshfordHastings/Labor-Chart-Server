package labor;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;


@Data
public class LaborSlot {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private Position position;
	
	@ManyToOne
	private Time time;
	
	@ManyToMany
	private List<Member> members;
	
}
