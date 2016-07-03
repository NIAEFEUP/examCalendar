package examination.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
public class Professor {

	private String id;
	ProfessorUnavailable unavailability;

	public Professor(String id){
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void finalize() throws Throwable {

	}

	public ProfessorUnavailable getUnavailability() {
		return unavailability;
	}

	public void setUnavailability(ProfessorUnavailable unavailability) {
		this.unavailability = unavailability;
	}


}