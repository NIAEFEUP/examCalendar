package examination.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:18
 */
public class Professor {
	private int id;
	private String name;
	private String initials;
	private String cod;

	public void finalize() throws Throwable {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitials() { return initials; }

	public void setInitials(String initials) { this.initials = initials; }

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}
}